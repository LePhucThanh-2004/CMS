package vn.edu.ntu.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ntu.cms.domain.entity.Article;
import vn.edu.ntu.cms.domain.entity.Comment;
import vn.edu.ntu.cms.domain.entity.User;
import vn.edu.ntu.cms.repository.ArticleRepository;
import vn.edu.ntu.cms.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<Comment> getArticleComments(Article article, Pageable pageable) {
        return commentRepository.findByArticleAndApprovedTrueAndParentIsNull(article, pageable);
    }

    @Transactional(readOnly = true)
    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findByParentIdAndApprovedTrue(parentId);
    }

    @Transactional
    public Comment createComment(Long articleId, String content, User author, Long parentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setArticle(article);
        comment.setAuthor(author);
        
        if (parentId != null) {
            commentRepository.findById(parentId).ifPresent(comment::setParent);
        }

        // Auto-approve comments by editors and admins
        if (author.getRoles().contains("EDITOR") || author.getRoles().contains("ADMIN")) {
            comment.setApproved(true);
        }

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment approveComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setApproved(true);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getPendingComments(Pageable pageable) {
        return commentRepository.findByApprovedFalse(pageable);
    }
} 