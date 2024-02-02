package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.ForumCategoryEntity;
import org.unibl.etf.repositories.ForumCategoryRepository;
import org.unibl.etf.services.ForumCategoryService;

import java.util.List;

@Service
@Transactional
public class ForumCategoryServiceImpl implements ForumCategoryService {

    private final ForumCategoryRepository forumCategoryRepository;

    public ForumCategoryServiceImpl(ForumCategoryRepository forumCategoryRepository) {
        this.forumCategoryRepository = forumCategoryRepository;
    }

    @Override
    public List<ForumCategoryEntity> findAll() {
        return this.forumCategoryRepository.findAll();
    }
}
