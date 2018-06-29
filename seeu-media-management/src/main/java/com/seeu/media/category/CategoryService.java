package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

	CategoryDto getCategory(Long categoryId);

	CategoryDto createCategory(CategoryDto categoryDto);

    void updateCategoryName(Long categoryId, String newName);

    void deleteCategory(Long categoryId);
}
