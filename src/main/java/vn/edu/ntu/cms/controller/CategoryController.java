package vn.edu.ntu.cms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.ntu.cms.domain.dto.CategoryDto;
import vn.edu.ntu.cms.domain.entity.Category;
import vn.edu.ntu.cms.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> categories = categoryService.findAllWithChildren();
        return ResponseEntity.ok(categories.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryDto>> getRootCategories() {
        List<Category> categories = categoryService.findRootCategories();
        return ResponseEntity.ok(categories.stream().map(this::mapToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
        return categoryService.findBySlug(slug)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = categoryService.createCategory(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                request.getParentId()
        );
        return ResponseEntity.ok(mapToDto(category));
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }
        dto.setChildren(category.getChildren().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static class CreateCategoryRequest {
        private String name;
        private String slug;
        private String description;
        private Long parentId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
} 