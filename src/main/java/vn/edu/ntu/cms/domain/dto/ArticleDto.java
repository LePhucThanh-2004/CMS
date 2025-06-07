package vn.edu.ntu.cms.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ArticleDto {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String summary;
    private String featuredImage;
    private Long categoryId;
    private String categoryName;
    private UserDto author;
    private Set<String> tags;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
} 