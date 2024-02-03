package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.AcceptCommentRequestDTO;
import org.unibl.etf.models.dto.CommentDTO;
import org.unibl.etf.models.dto.CommentRequestDTO;
import org.unibl.etf.models.entities.ForumCategoryEntity;
import org.unibl.etf.services.CommentService;
import org.unibl.etf.services.ForumCategoryService;
import org.unibl.etf.services.WAFService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forum")
@CrossOrigin
public class ForumController {

    private final ForumCategoryService forumCategoryService;
    private final CommentService commentService;
    private final WAFService wafService;

    public ForumController(ForumCategoryService forumCategoryService, CommentService commentService, WAFService wafService) {
        this.forumCategoryService = forumCategoryService;
        this.commentService = commentService;
        this.wafService = wafService;
    }

    @GetMapping("/categories")
    public List<ForumCategoryEntity> findAll(){
        return this.forumCategoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public ForumCategoryEntity findById(@PathVariable Long id){
        return this.forumCategoryService.findById(id);
    }

    @GetMapping("/{id}/comments")
    public List<CommentDTO> findAllCommentByCategoryId(@PathVariable Long id){
        return this.commentService.findAllByCategoryId(id);
    }

    @GetMapping("/comments/pending")
    public List<CommentDTO> findAllPending(){
        return this.commentService.findAllPendingComment();
    }

    @PutMapping("/comments/{id}/accept")
    public void acceptComment(@PathVariable Long id, @Valid @RequestBody AcceptCommentRequestDTO requestDTO, BindingResult bindingResult){
        this.wafService.checkRequest(bindingResult);
        this.commentService.acceptComment(id,requestDTO);
    }
    @DeleteMapping("/comments/{id}/decline")
    public void declineComment(@PathVariable Long id){
        this.commentService.declineComment(id);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendCommentForVerification(@PathVariable Long id, @Valid @RequestBody CommentRequestDTO requestDTO,BindingResult bindingResult, Authentication authentication){
        this.wafService.checkRequest(bindingResult);
        this.commentService.sendForVerification(id,requestDTO,authentication);
    }

    @PutMapping("/{id}/comments/{commentId}")
    public void editComment(@PathVariable Long id,@PathVariable Long commentId,@Valid @RequestBody CommentRequestDTO requestDTO,BindingResult bindingResult,Authentication authentication){
        this.wafService.checkRequest(bindingResult);
        this.commentService.editComment(commentId,requestDTO,authentication);
    }

    @DeleteMapping("/{id}/comments/{commentId}/{clientId}")
    public void deleteComment(@PathVariable Long id,@PathVariable Long commentId,@PathVariable Long clientId,Authentication authentication){
        this.commentService.deleteComment(commentId,clientId,authentication);
    }
}
