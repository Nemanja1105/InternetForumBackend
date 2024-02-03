package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.ForumCategoryEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String comment;
    private Date createdAt;
    private UserBasicInfoDTO sender;
    private Long forumCategoryId;
    private String forumCategoryName;
    private boolean approved;
}
