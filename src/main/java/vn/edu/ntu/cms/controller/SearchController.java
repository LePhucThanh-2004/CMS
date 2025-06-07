package vn.edu.ntu.cms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.ntu.cms.domain.dto.ArticleDto;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.service.SearchService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/articles")
    public ResponseEntity<Page<ArticleDto>> searchArticles(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Set<String> tags,
            Pageable pageable) {
        Page<Article> articles = searchService.searchArticles(query, categoryId, tags, pageable);
        return ResponseEntity.ok(articles.map(this::mapToArticleDto));
    }

    @GetMapping("/tags/suggest")
    public ResponseEntity<List<String>> suggestTags(@RequestParam String prefix) {
        List<String> suggestions = searchService.suggestTags(prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<String>> getPopularSearchTerms() {
        List<String> popularTerms = searchService.getPopularSearchTerms();
        return ResponseEntity.ok(popularTerms);
    }

    private ArticleDto mapToArticleDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSlug(article.getSlug());
        dto.setSummary(article.getSummary());
        dto.setFeaturedImage(article.getFeaturedImage());
        
        if (article.getCategory() != null) {
            dto.setCategoryId(article.getCategory().getId());
            dto.setCategoryName(article.getCategory().getName());
        }
        
        dto.setTags(article.getTags().stream()
                .map(tag -> tag.getName())
                .collect(java.util.stream.Collectors.toSet()));
        
        dto.setPublished(article.isPublished());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setPublishedAt(article.getPublishedAt());
        
        return dto;
    }
} 