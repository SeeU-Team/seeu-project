package com.seeu.darkside.asset;

import java.util.List;

/**
 * Created by Robin on 05/07/2018.
 */
public interface AssetService {
	void deleteAll(Long teamId);

	void saveAll(List<TeamHasAssetEntity> teamHasAssetToSave);

	List<TeamHasAssetEntity> extractAssets(List<Asset> assets, Long idTeam);

	List<AssetDto> getAssetEntitiesFromIds(List<TeamHasAssetEntity> teamHasAssetEntities);

	List<TeamHasAssetEntity> findAllByTeamId(Long idTeam);
}
