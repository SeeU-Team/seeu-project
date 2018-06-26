package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryEntity createCategory(CategoryDTO categoryDTO);

    CategoryEntity getCategory(Long categoryId);

    void updateCategoryName(Long categoryId, String newName);

    void deleteCategory(Long categoryId);

    List<CategoryEntity> getAllCategories();
}
