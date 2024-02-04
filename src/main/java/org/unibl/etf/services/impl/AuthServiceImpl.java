package org.unibl.etf.services.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.models.entities.UserEntity;
import org.unibl.etf.models.enums.Role;
import org.unibl.etf.repositories.UserRepository;
import org.unibl.etf.services.AuthService;
import org.unibl.etf.services.EmailService;
import org.unibl.etf.services.JwtService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;
    private final JwtService jwtService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${auth.github.client_id}")
    private String clientId;
    @Value("${auth.github.client_secret}")
    private String clientSecret;
    @Value("${auth.github.redirect_uri}")
    private String redirectUrl;

    @Value("${auth.github.access_token_url}")
    private String accessTokenUrl;

    @Value("${auth.github.user_api}")
    private String userDetailsGithubUrl;
    private Random random = new Random();

    public AuthServiceImpl(UserRepository userRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @Override
    public void registerClient(RegisterUserDTO request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistsException();
        var entity = mapper.map(request, UserEntity.class);
        entity.setId(null);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(Role.CLIENT);
        entity.setStatus(true);
        entity.setVerified(false);
        entity = userRepository.saveAndFlush(entity);
    }

    @Override
    public boolean checkDetails(CheckDetailsDTO checkDetailsDTO) {
        if (checkDetailsDTO.getColumn().equals("email") || checkDetailsDTO.getColumn().equals("clientEmail")) {
            return userRepository.existsByEmail(checkDetailsDTO.getValue());
        } else if (checkDetailsDTO.getColumn().equals("username") || checkDetailsDTO.getColumn().equals("clientUsername")) {
            return userRepository.existsByUsername(checkDetailsDTO.getValue());
        }
        return false;
    }

    @Override
    public Long login(LoginRequestDTO requestDTO) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
        UserEntity userEntity = this.userRepository.findById(user.getId()).orElseThrow(NotFoundException::new);
        if (!userEntity.isStatus()) {
            throw new AccountBlockedException();
        }
        if (!userEntity.isVerified()) {
            throw new NotApprovedException();
        }
        var code = this.generateCode();
        userEntity.setLoginCode(code);
        this.emailService.sendVerificationMail(code, userEntity.getEmail());
        return user.getId();
    }

    @Override
    public UserDTO finishLogin(VerificationRequestDTO requestDTO) {
        UserEntity userEntity = this.userRepository.findById(requestDTO.getId()).orElseThrow(NotFoundException::new);
        if (userEntity.getLoginCode() == null)
            throw new UnauthorizedException();
        if (!userEntity.getLoginCode().equals(requestDTO.getCode()))
            throw new UnauthorizedException();
        if (!userEntity.isStatus()) {
            throw new AccountBlockedException();
        }
        var response = mapper.map(userEntity, UserDTO.class);
        response.setPermissions(userEntity.getPermissions().stream().map(PermissionEntity::getName).collect(Collectors.toList()));
        var token = jwtService.generateToken(mapper.map(userEntity, JwtUserDTO.class));
        response.setToken(token);
        userEntity.setLoginCode(null);
        return response;
    }

    @Override//TODO REFACTOR
    public UserDTO loginWithGithub(String code) throws URISyntaxException, IOException {

            URI uri = new URIBuilder(accessTokenUrl)
                    .addParameter("client_id", clientId)
                    .addParameter("client_secret", clientSecret)
                    .addParameter("code", code)
                    .addParameter("redirect_uri", redirectUrl)
                    .build();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setEntity(new StringEntity("", ContentType.APPLICATION_FORM_URLENCODED));
            HttpResponse response = client.execute(postRequest);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            String accessToken = this.extractAccessToken(responseBody);
            String userDetails = this.getUserDetails(accessToken);
            ObjectMapper jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            var githubUser = jsonMapper.readValue(userDetails, GithubUserDTO.class);
            var userEntity = this.registerOrLoad(githubUser);
            if (!userEntity.isStatus()) {
                throw new AccountBlockedException();
            }
            var responseUser = mapper.map(userEntity, UserDTO.class);
            responseUser.setPermissions(userEntity.getPermissions().stream().map(PermissionEntity::getName).collect(Collectors.toList()));
            var token = jwtService.generateToken(mapper.map(userEntity, JwtUserDTO.class));
            responseUser.setToken(token);
            return responseUser;



    }

    private UserEntity registerOrLoad(GithubUserDTO user) {
        System.out.println(user.getEmail());
        if (this.userRepository.existsByEmail(user.getEmail())) {
            return this.userRepository.findByEmail(user.getEmail()).get();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(null);
        userEntity.setUsername(user.getLogin());
        userEntity.setPassword(null);
        userEntity.setEmail(user.getEmail());
        if (user.getName() != null) {
            var tmp = user.getName().split(" ");
            userEntity.setName(tmp[0]);
            userEntity.setSurname(tmp[1]);
        } else {
            userEntity.setName(user.getLogin());
            userEntity.setSurname(user.getLogin());
        }
        userEntity.setRole(Role.CLIENT);
        userEntity.setStatus(true);
        userEntity.setVerified(true);
        userEntity = this.userRepository.saveAndFlush(userEntity);
        this.entityManager.refresh(userEntity);
        return userEntity;
    }

    private String getUserDetails(String accessToken) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(userDetailsGithubUrl)
                .build();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(uri);
        getRequest.setHeader("Authorization", "Bearer " + accessToken);
        HttpResponse response = client.execute(getRequest);
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return responseBody;
    }

    private String extractAccessToken(String responseBody) {
        String prefix = "access_token=";
        String suffix = "&scope";
        int startIndex = responseBody.indexOf(prefix);
        int endIndex = responseBody.indexOf(suffix);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return responseBody.substring(startIndex + prefix.length(), endIndex);
        }
        return null; // Return null if the access token cannot be extracted
    }

    private String generateCode() {
        int randomNumber = random.nextInt(9000) + 1000;
        return "" + randomNumber;
    }


}
