package vn.edu.ntu.cms.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long articleId;
    private UserDto author;
    private Long parentId;
    private boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 