package com.seeu.darkside.asset;

import com.seeu.darkside.rs.dto.TeamCreation;

import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
public interface AssetService {
	void saveAll(List<TeamHasAssetEntity> teamHasAssetToSave);

	List<TeamHasAssetEntity> extractAssets(TeamCreation teamCreation, Long idTeam);

	List<AssetEntity> getAssetEntitiesFromIds(List<TeamHasAssetEntity> teamHasAssetEntities);

	List<TeamHasAssetEntity> findAllByTeamId(Long idTeam);
}
