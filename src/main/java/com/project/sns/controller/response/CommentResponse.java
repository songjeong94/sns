package com.project.sns.controller.response;

import com.project.sns.model.Comment;
import com.project.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class CommentResponse {

    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getUserName(),
                comment.getPostId(),
                comment.getRegisteredAt(),
                comment.getUpdatedAt(),
                comment.getDeletedAt()
        );
    }
}
