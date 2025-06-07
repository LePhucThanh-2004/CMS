package vn.edu.ntu.cms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    Page<Article> findByPublishedTrue(Pageable pageable);
    Page<Article> findByCategoryAndPublishedTrue(Category category, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.published = true AND (LOWER(a.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(a.content) LIKE LOWER(CONCAT('%', ?1, '%')))")
    Page<Article> searchArticles(String query, Pageable pageable);
    
    List<Article> findTop5ByPublishedTrueOrderByPublishedAtDesc();
} 