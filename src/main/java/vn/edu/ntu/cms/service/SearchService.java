package vn.edu.ntu.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.repository.ArticleRepository;
import vn.edu.ntu.cms.repository.CategoryRepository;
import vn.edu.ntu.cms.repository.TagRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Page<Article> searchArticles(String query, Long categoryId, Set<String> tags, Pageable pageable) {
        // If no filters are applied, use the basic search
        if (categoryId == null && (tags == null || tags.isEmpty())) {
            return articleRepository.searchArticles(query, pageable);
        }
        // This is a placeholder for demonstration
        return articleRepository.searchArticles(query, pageable);
    }

    @Transactional(readOnly = true)
    public List<String> suggestTags(String prefix) {
    
        return List.of(); // Placeholder
    }

    @Transactional(readOnly = true)
    public List<String> getPopularSearchTerms() {
        return List.of(); // Placeholder
    }
} 