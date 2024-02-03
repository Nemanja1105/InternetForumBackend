package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.AcceptCommentRequestDTO;
import org.unibl.etf.models.dto.CommentDTO;
import org.unibl.etf.models.dto.CommentRequestDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findAllByCategoryId(Long id);
    void sendForVerification(Long categoryId, CommentRequestDTO requestDTO, Authentication authentication);
    void editComment(Long commentId,CommentRequestDTO requestDTO,Authentication authentication);
    void deleteComment(Long commentId,Long clientId,Authentication authentication);
    List<CommentDTO> findAllPendingComment();
    void acceptComment(Long id, AcceptCommentRequestDTO requestDTO);
    void declineComment(Long id);
}
