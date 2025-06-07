package vn.edu.ntu.cms.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private List<CategoryDto> children = new ArrayList<>();
} 