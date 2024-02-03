package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.AcceptCommentRequestDTO;
import org.unibl.etf.models.dto.CommentDTO;
import org.unibl.etf.models.dto.CommentRequestDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.entities.CommentEntity;
import org.unibl.etf.models.entities.ForumCategoryEntity;
import org.unibl.etf.models.entities.UserEntity;
import org.unibl.etf.repositories.CommentRepository;
import org.unibl.etf.repositories.ForumCategoryRepository;
import org.unibl.etf.services.CommentService;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private static final Integer PAGE_SIZE=20;
    private final CommentRepository commentRepository;
    private final ForumCategoryRepository forumCategoryRepository;
    private final ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, ForumCategoryRepository forumCategoryRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.forumCategoryRepository = forumCategoryRepository;
        this.mapper = mapper;
    }


    @Override
    public List<CommentDTO> findAllByCategoryId(Long id) {
        if(!forumCategoryRepository.existsById(id))
            throw new NotFoundException();
        Pageable topTwenty = PageRequest.of(0,PAGE_SIZE);
        return this.commentRepository.findByForumCategoryIdAndApprovedOrderByCreatedAtDesc(id,true,topTwenty).stream()
            .map(el->mapper.map(el, CommentDTO.class)).toList();
    }

    @Override
    public void sendForVerification(Long categoryId, CommentRequestDTO requestDTO, Authentication authentication) {
        if(!forumCategoryRepository.existsById(categoryId))
            throw new NotFoundException();
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(requestDTO.getSenderId())) {
            throw new UnauthorizedException();
        }
        var entity=mapper.map(requestDTO, CommentEntity.class);
        entity.setId(null);
        entity.setCreatedAt(new Date());
        ForumCategoryEntity forumCategory=new ForumCategoryEntity(); forumCategory.setId(categoryId);
        entity.setForumCategory(forumCategory);
        UserEntity userEntity=new UserEntity(); userEntity.setId(requestDTO.getSenderId());
        entity.setSender(userEntity);
        entity.setApproved(false);
        this.commentRepository.saveAndFlush(entity);

    }

    @Override
    public void editComment(Long commentId, CommentRequestDTO requestDTO, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(requestDTO.getSenderId())) {
            throw new UnauthorizedException();
        }
        var comment=this.commentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        comment.setComment(requestDTO.getComment());
        comment.setCreatedAt(new Date());
        comment.setApproved(false);
    }

    @Override
    public void deleteComment(Long commentId, Long clientId,Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId)) {
            throw new UnauthorizedException();
        }
        if(!this.commentRepository.existsById(commentId))
            throw new NotFoundException();
        this.commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDTO> findAllPendingComment() {
        return this.commentRepository.findByApprovedOrderByCreatedAtDesc(false).stream().map(el->mapper.map(el,CommentDTO.class)).toList();
    }

    @Override
    public void acceptComment(Long id, AcceptCommentRequestDTO requestDTO) {
        var comment=this.commentRepository.findById(id).orElseThrow(NotFoundException::new);
        comment.setComment(requestDTO.getMessage());
        comment.setApproved(true);
    }

    @Override
    public void declineComment(Long id) {
        if(!this.commentRepository.existsById(id))
            throw new NotFoundException();
        this.commentRepository.deleteById(id);
    }


}
