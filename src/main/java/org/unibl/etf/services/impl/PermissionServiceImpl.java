package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.PermissionEntity;
import org.unibl.etf.repositories.PermissionRepository;
import org.unibl.etf.services.PermissionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<PermissionEntity> findAll() {
        return this.permissionRepository.findAll();
    }
}
