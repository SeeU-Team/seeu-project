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
        return null;
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return null;
    }
}
