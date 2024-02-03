package org.unibl.etf.services.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.JwtBlackListEntity;
import org.unibl.etf.repositories.JwtBlackListRepository;
import org.unibl.etf.services.JwtBlackListService;
import org.unibl.etf.services.JwtService;

import java.util.List;

@Service
public class JwtBlackListServiceImpl implements JwtBlackListService {





    private final JwtBlackListRepository jwtBlackListRepository;

    public JwtBlackListServiceImpl(JwtBlackListRepository jwtBlackListRepository) {

        this.jwtBlackListRepository = jwtBlackListRepository;
    }

    @Override
    public void blackList(String token) {
        JwtBlackListEntity entity=new JwtBlackListEntity();
        entity.setId(null);
        entity.setToken(token);
        this.jwtBlackListRepository.saveAndFlush(entity);
    }

    @Override
    public boolean isInBlacklist(String token) {
        return this.jwtBlackListRepository.existsByToken(token);
    }

    @Override
    public List<JwtBlackListEntity> findAll() {
        return this.jwtBlackListRepository.findAll();
    }

    @Override
    public void delete(JwtBlackListEntity entity) {
        this.jwtBlackListRepository.delete(entity);
    }
}
