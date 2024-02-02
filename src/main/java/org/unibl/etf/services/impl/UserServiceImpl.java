package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.models.dto.ChangeUserPermissionsDTO;
import org.unibl.etf.models.dto.ChangeUserRoleDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.dto.UserDetailsDTO;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.models.enums.Role;
import org.unibl.etf.repositories.UserRepository;
import org.unibl.etf.services.EmailService;
import org.unibl.etf.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    @Override
    public List<UserDetailsDTO> findAll(Authentication authentication) {
        var jwtUser=(JwtUserDTO)authentication.getPrincipal();
        return this.userRepository.findByIdNot(jwtUser.getId()).stream().map(el->mapper.map(el,UserDetailsDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void approveUser(Long id) {
        var entity=this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        entity.setVerified(true);
        this.emailService.sendApprovalInfo(entity.getEmail());
    }

    @Override
    public void blockUnblockUser(Long id) {
        var entity=this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        entity.setStatus(!entity.isStatus());
    }

    @Override
    public void changeRole(Long id, ChangeUserRoleDTO request) {
        var entity=this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        entity.setRole(Role.getByStatus(request.getRole()));
    }

    @Override
    public void changePermissions(Long id, ChangeUserPermissionsDTO request) {
        var entity=this.userRepository.findById(id).orElseThrow(NotFoundException::new);
        entity.getPermissions().clear();
        for(var perm:request.getPermissions()){
            var tmp=new PermissionEntity(); tmp.setId(perm);
            entity.getPermissions().add(tmp);
        }
    }
}
