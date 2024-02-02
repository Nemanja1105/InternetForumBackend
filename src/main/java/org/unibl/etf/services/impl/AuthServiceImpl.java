package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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
        var response = mapper.map(userEntity, UserDTO.class);
        response.setPermissions(userEntity.getPermissions().stream().map(PermissionEntity::getName).collect(Collectors.toList()));
        var token = jwtService.generateToken(mapper.map(userEntity, JwtUserDTO.class));
        response.setToken(token);
        userEntity.setLoginCode(null);
        return response;
    }

    private String generateCode() {
        int randomNumber = random.nextInt(9000) + 1000;
        return "" + randomNumber;
    }


}
