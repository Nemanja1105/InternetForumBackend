package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.services.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@CrossOrigin
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public List<PermissionEntity> findAll(){
        return this.permissionService.findAll();
    }
}
