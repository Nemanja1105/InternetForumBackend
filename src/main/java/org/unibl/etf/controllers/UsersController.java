package org.unibl.etf.controllers;


import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.ChangeUserPermissionsDTO;
import org.unibl.etf.models.dto.ChangeUserRoleDTO;
import org.unibl.etf.models.dto.UserDetailsDTO;
import org.unibl.etf.services.UserService;
import org.unibl.etf.services.WAFService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UsersController {

    private final UserService userService;
    private final WAFService wafService;

    public UsersController(UserService userService, WAFService wafService) {
        this.userService = userService;
        this.wafService = wafService;
    }

    @GetMapping
    public List<UserDetailsDTO> findAll(Authentication auth){
        return this.userService.findAll(auth);
    }

    @PutMapping("/{id}/approve")
    public void approveClient(@PathVariable Long id){
        this.userService.approveUser(id);
    }

    @PutMapping("/{id}/block-unblock")
    public void blockUnblock(@PathVariable Long id){
        this.userService.blockUnblockUser(id);
    }

    @PutMapping("/{id}/role")
    public void changeRole(@PathVariable Long id, @Valid @RequestBody ChangeUserRoleDTO roleDTO, BindingResult bindingResult){
        this.wafService.checkRequest(bindingResult);
        this.userService.changeRole(id,roleDTO);
    }

    @PutMapping("/{id}/permissions")
    public void changePermissions(@PathVariable Long id, @Valid @RequestBody ChangeUserPermissionsDTO permissionsDTO,BindingResult bindingResult){
        this.wafService.checkRequest(bindingResult);
        this.userService.changePermissions(id,permissionsDTO);
    }
}
