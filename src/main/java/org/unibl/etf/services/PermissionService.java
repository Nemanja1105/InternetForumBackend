package org.unibl.etf.services;

import org.unibl.etf.models.entities.PermissionEntity;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> findAll();
}
