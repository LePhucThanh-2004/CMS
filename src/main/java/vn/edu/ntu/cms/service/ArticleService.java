package vn.edu.ntu.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.Category;
import vn.edu.ntu.cms.domain.entity.Tag;
import vn.edu.ntu.cms.domain.entity.User;
import vn.edu.ntu.cms.repository.ArticleRepository;
import vn.edu.ntu.cms.repository.CategoryRepository;
import vn.edu.ntu.cms.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Article> findAllPublished(Pageable pageable) {
        return articleRepository.findByPublishedTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Article> findBySlug(String slug) {
        return articleRepository.findBySlug(slug);
    }

    @Transactional
    public Article createArticle(String title, String slug, String content, String summary,
                               String featuredImage, Long categoryId, User author, Set<String> tagNames) {
        Article article = new Article();
        article.setTitle(title);
        article.setSlug(slug);
        article.setContent(content);
        article.setSummary(summary);
        article.setFeaturedImage(featuredImage);
        article.setAuthor(author);

        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(article::setCategory);
        }

        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> {
                            Tag tag = new Tag();
                            tag.setName(name);
                            tag.setSlug(name.toLowerCase().replace(' ', '-'));
                            return tagRepository.save(tag);
                        }))
                .collect(Collectors.toSet());
        article.setTags(tags);

        return articleRepository.save(article);
    }

    @Transactional
    public Article publishArticle(Article article) {
        article.setPublished(true);
        article.setPublishedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public Page<Article> searchArticles(String query, Pageable pageable) {
        return articleRepository.searchArticles(query, pageable);
    }

    @Transactional(readOnly = true)
    public List<Article> findRecentArticles() {
        return articleRepository.findTop5ByPublishedTrueOrderByPublishedAtDesc();
    }
} 