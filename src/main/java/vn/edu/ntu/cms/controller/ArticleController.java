package vn.edu.ntu.cms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.ntu.cms.domain.dto.ArticleDto;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.User;
import vn.edu.ntu.cms.service.ArticleService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getAllPublishedArticles(Pageable pageable) {
        Page<Article> articles = articleService.findAllPublished(pageable);
        return ResponseEntity.ok(articles.map(this::mapToDto));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleDto> getArticleBySlug(@PathVariable String slug) {
        return articleService.findBySlug(slug)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<ArticleDto> createArticle(
            @Valid @RequestBody CreateArticleRequest request,
            @AuthenticationPrincipal User currentUser) {
        Article article = articleService.createArticle(
                request.getTitle(),
                request.getSlug(),
                request.getContent(),
                request.getSummary(),
                request.getFeaturedImage(),
                request.getCategoryId(),
                currentUser,
                request.getTags()
        );
        return ResponseEntity.ok(mapToDto(article));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<ArticleDto> publishArticle(@PathVariable Long id) {
        return articleService.findById(id)
                .map(articleService::publishArticle)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ArticleDto>> searchArticles(
            @RequestParam String query,
            Pageable pageable) {
        Page<Article> articles = articleService.searchArticles(query, pageable);
        return ResponseEntity.ok(articles.map(this::mapToDto));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ArticleDto>> getRecentArticles() {
        List<Article> articles = articleService.findRecentArticles();
        return ResponseEntity.ok(articles.stream().map(this::mapToDto).toList());
    }

    private ArticleDto mapToDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setContent(article.getContent());
        dto.setSummary(article.getSummary());
        dto.setFeaturedImage(article.getFeaturedImage());
        if (article.getCategory() != null) {
            dto.setCategoryId(article.getCategory().getId());
            dto.setCategoryName(article.getCategory().getName());
        }
        dto.setTags(article.getTags().stream().map(tag -> tag.getName()).collect(java.util.stream.Collectors.toSet()));
        dto.setPublished(article.isPublished());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());
        dto.setPublishedAt(article.getPublishedAt());
        return dto;
    }

    public static class CreateArticleRequest {
        private String title;
        private String slug;
        private String content;
        private String summary;
        private String featuredImage;
        private Long categoryId;
        private Set<String> tags;

        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getFeaturedImage() { return featuredImage; }
        public void setFeaturedImage(String featuredImage) { this.featuredImage = featuredImage; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public Set<String> getTags() { return tags; }
        public void setTags(Set<String> tags) { this.tags = tags; }
    }
} 