package com.seeu.darkside.category;

import com.seeu.darkside.rs.dto.TeamCreation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Robin on 05/07/2018.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	private final TeamHasCategoryRepository teamHasCategoryRepository;
	private final CategoryServiceProxy categoryServiceProxy;

	public CategoryServiceImpl(TeamHasCategoryRepository teamHasCategoryRepository, CategoryServiceProxy categoryServiceProxy) {
		this.teamHasCategoryRepository = teamHasCategoryRepository;
		this.categoryServiceProxy = categoryServiceProxy;
	}

	@Override
	public void deleteAll(Long teamId) {
		List<TeamHasCategoryEntity> allByTeamId = teamHasCategoryRepository.findAllByTeamId(teamId);
		teamHasCategoryRepository.deleteAll(allByTeamId);
	}

	@Override
	public void saveAll(List<TeamHasCategoryEntity> teamHasCategoryToSave) {
		teamHasCategoryRepository.saveAll(teamHasCategoryToSave);
	}

	@Override
	public List<CategoryEntity> getCategoryEntitiesFromIds(List<TeamHasCategoryEntity> categoryEntitiesIds) {
		List<CategoryEntity> categoryEntities = new ArrayList<>();
		for (TeamHasCategoryEntity categoryEntityId : categoryEntitiesIds)
			categoryEntities.add(categoryServiceProxy.getCategoryInfo(categoryEntityId.getCategoryId()));
		return categoryEntities;
	}

	@Override
	public List<TeamHasCategoryEntity> extractCategories(List<Category> categories, Long idTeam) {
		if (null == categories) {
			return new ArrayList<>();
		}

		return categories
				.stream()
				.map(category -> TeamHasCategoryEntity.builder()
						.teamId(idTeam)
						.categoryId(category.getIdCategory())
						.build())
				.collect(toList());
	}

	@Override
	public List<TeamHasCategoryEntity> findAllByTeamId(Long idTeam) {
		return teamHasCategoryRepository.findAllByTeamId(idTeam);
	}

	@Override
	public List<TeamHasCategoryEntity> findAllByCategoryId(Long categoryId) {
		return teamHasCategoryRepository.findAllByCategoryId(categoryId);
	}
}
