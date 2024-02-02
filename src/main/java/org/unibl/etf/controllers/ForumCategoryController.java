package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.entities.ForumCategoryEntity;
import org.unibl.etf.services.ForumCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum/categories")
@CrossOrigin
public class ForumCategoryController {

    private final ForumCategoryService forumCategoryService;

    public ForumCategoryController(ForumCategoryService forumCategoryService) {
        this.forumCategoryService = forumCategoryService;
    }

    @GetMapping
    public List<ForumCategoryEntity> findAll(){
        return this.forumCategoryService.findAll();
    }
}
