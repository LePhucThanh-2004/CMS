package vn.edu.ntu.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ntu.cms.domain.entity.Category;
import vn.edu.ntu.cms.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAllWithChildren() {
        return categoryRepository.findAllWithChildren();
    }

    @Transactional(readOnly = true)
    public Optional<Category> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Transactional
    public Category createCategory(String name, String slug, String description, Long parentId) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);

        if (parentId != null) {
            categoryRepository.findById(parentId).ifPresent(category::setParent);
        }

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findRootCategories() {
        return categoryRepository.findByParentIsNull();
    }
} 