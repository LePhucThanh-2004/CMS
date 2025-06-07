package vn.edu.ntu.cms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.ntu.cms.domain.dto.CommentDto;
import vn.edu.ntu.cms.domain.dto.UserDto;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.Comment;
import vn.edu.ntu.cms.domain.entity.User;
import vn.edu.ntu.cms.service.ArticleService;
import vn.edu.ntu.cms.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ArticleService articleService;

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Page<CommentDto>> getArticleComments(
            @PathVariable Long articleId,
            Pageable pageable) {
        Article article = articleService.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        
        Page<Comment> comments = commentService.getArticleComments(article, pageable);
        return ResponseEntity.ok(comments.map(this::mapToDto));
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentDto>> getCommentReplies(@PathVariable Long commentId) {
        List<Comment> replies = commentService.getReplies(commentId);
        return ResponseEntity.ok(replies.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    @PostMapping("/article/{articleId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal User currentUser) {
        Comment comment = commentService.createComment(
                articleId,
                request.getContent(),
                currentUser,
                request.getParentId()
        );
        return ResponseEntity.ok(mapToDto(comment));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<CommentDto> approveComment(@PathVariable Long id) {
        Comment comment = commentService.approveComment(id);
        return ResponseEntity.ok(mapToDto(comment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<CommentDto>> getPendingComments(Pageable pageable) {
        Page<Comment> comments = commentService.getPendingComments(pageable);
        return ResponseEntity.ok(comments.map(this::mapToDto));
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setArticleId(comment.getArticle().getId());
        
        UserDto authorDto = new UserDto();
        authorDto.setId(comment.getAuthor().getId());
        authorDto.setUsername(comment.getAuthor().getUsername());
        authorDto.setFullName(comment.getAuthor().getFullName());
        dto.setAuthor(authorDto);

        if (comment.getParent() != null) {
            dto.setParentId(comment.getParent().getId());
        }
        
        dto.setApproved(comment.isApproved());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    public static class CreateCommentRequest {
        private String content;
        private Long parentId;

        // Getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
} 