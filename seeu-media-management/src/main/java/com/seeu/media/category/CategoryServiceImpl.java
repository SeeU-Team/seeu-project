package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryAdapter categoryAdapter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryAdapter categoryAdapter) {
        this.categoryRepository = categoryRepository;
		this.categoryAdapter = categoryAdapter;
	}

    @Override
	@Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
				.stream()
				.map(categoryAdapter::entityToDto)
				.collect(Collectors.toList());
    }

    @Override
	@Transactional(readOnly = true)
    public CategoryDto getCategory(Long categoryId) {
        CategoryEntity entity = categoryRepository.getOne(categoryId);
        return categoryAdapter.entityToDto(entity);
    }

    @Override
	@Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Date now = new Date();

        CategoryEntity entity = categoryAdapter.dtoToEntity(categoryDto);
        entity.setCreated(now);
        entity.setUpdated(now);

        entity = categoryRepository.save(entity);

		return categoryAdapter.entityToDto(entity);
    }

    @Override
	@Transactional
    public void updateCategoryName(Long categoryId, String newName) {
        CategoryEntity categoryToUpdate = categoryRepository.getOne(categoryId);
        Date date = new Date();
        categoryToUpdate.setName(newName);
        categoryToUpdate.setUpdated(date);
        categoryRepository.save(categoryToUpdate);
    }

    @Override
	@Transactional
    public void deleteCategory(Long categoryId) {
        CategoryEntity categoryToDelete = categoryRepository.getOne(categoryId);
        categoryRepository.delete(categoryToDelete);
    }
}
