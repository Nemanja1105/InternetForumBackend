package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.JwtBlackListEntity;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackListEntity,Long> {
    boolean existsByToken(String token);
}
