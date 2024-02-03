package org.unibl.etf.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.CommentEntity;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findByForumCategoryIdAndApprovedOrderByCreatedAtDesc(Long id,boolean approved, Pageable page);
    List<CommentEntity> findByApprovedOrderByCreatedAtDesc(boolean approved);
}
