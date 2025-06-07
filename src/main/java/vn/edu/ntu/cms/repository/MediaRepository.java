package vn.edu.ntu.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.ntu.cms.domain.entity.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    void deleteByFileName(String fileName);
} 