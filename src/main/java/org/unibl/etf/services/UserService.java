package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.ChangeUserPermissionsDTO;
import org.unibl.etf.models.dto.ChangeUserRoleDTO;
import org.unibl.etf.models.dto.UserDetailsDTO;

import java.util.List;

public interface UserService {
    List<UserDetailsDTO> findAll(Authentication authentication);
    void approveUser(Long id);
    void blockUnblockUser(Long id);
    void changeRole(Long id,ChangeUserRoleDTO request);

    void changePermissions(Long id, ChangeUserPermissionsDTO request);
}
