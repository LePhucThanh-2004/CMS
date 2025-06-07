package vn.edu.ntu.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.ntu.cms.domain.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findBySlug(String slug);
    Optional<Tag> findByName(String name);
} 