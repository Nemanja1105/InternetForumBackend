package org.unibl.etf.services;

import org.unibl.etf.models.entities.JwtBlackListEntity;

import java.util.List;

public interface JwtBlackListService {
    void blackList(String token);
    boolean isInBlacklist(String token);
    List<JwtBlackListEntity> findAll();
    void delete(JwtBlackListEntity entity);
}
