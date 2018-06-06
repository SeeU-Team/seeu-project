package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryEntity createCategory(CategoryDTO categoryDTO);

    CategoryEntity getCategory(Long categoryId);

    List<CategoryEntity> getAllCategories();
}
