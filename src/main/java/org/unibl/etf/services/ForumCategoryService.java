package org.unibl.etf.services;

import org.unibl.etf.models.entities.ForumCategoryEntity;

import java.util.List;

public interface ForumCategoryService {
    List<ForumCategoryEntity> findAll();
}
