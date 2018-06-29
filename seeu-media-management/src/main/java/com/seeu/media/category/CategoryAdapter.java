package com.seeu.media.category;

import com.seeu.media.rs.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryAdapter {

	public CategoryDto entityToDto(CategoryEntity entity) {
		return CategoryDto.builder()
				.id(entity.getIdCategory())
				.name(entity.getName())
				.build();
	}

	public CategoryEntity dtoToEntity(CategoryDto dto) {
		return CategoryEntity.builder()
				.idCategory(dto.getId())
				.name(dto.getName())
				.build();
	}
}
