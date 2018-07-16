package com.seeu.darkside.category;

import com.seeu.darkside.rs.dto.TeamCreation;

import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
public interface CategoryService {
	void deleteAll(Long teamId);

	void saveAll(List<TeamHasCategoryEntity> teamHasCategoryToSave);

	List<CategoryEntity> getCategoryEntitiesFromIds(List<TeamHasCategoryEntity> categoryEntitiesIds);

	List<TeamHasCategoryEntity> extractCategories(List<Category> categories, Long idTeam);

	List<TeamHasCategoryEntity> findAllByTeamId(Long idTeam);

	List<TeamHasCategoryEntity> findAllByCategoryId(Long categoryId);

	void deleteTeamHasCategory(Long id);
}
