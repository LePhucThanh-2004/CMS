package vn.edu.ntu.cms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticleAndApprovedTrueAndParentIsNull(Article article, Pageable pageable);
    List<Comment> findByParentIdAndApprovedTrue(Long parentId);
    Page<Comment> findByApprovedFalse(Pageable pageable);
} 