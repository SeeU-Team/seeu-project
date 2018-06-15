package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryEntity createCategory(CategoryDTO categoryDTO) {
        Date now = new Date();

        CategoryEntity entityToCreate = CategoryEntity.builder()
                .name(categoryDTO.getName())
                .created(now)
                .updated(now)
                .build();

        return categoryRepository.save(entityToCreate);
    }

    @Override
    public CategoryEntity getCategory(Long categoryId) {
        return categoryRepository.findCategoryEntityByIdCategory(categoryId);
    }

    @Override
    public void updateCategoryName(Long categoryId, String newName) {
        CategoryEntity categoryToUpdate = categoryRepository.findCategoryEntityByIdCategory(categoryId);
        categoryToUpdate.setName(newName);
        categoryRepository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        CategoryEntity categoryToDelete = categoryRepository.findCategoryEntityByIdCategory(categoryId);
        categoryRepository.delete(categoryToDelete);
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
