package com.seeu.darkside.asset;

import com.seeu.darkside.rs.dto.TeamCreation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Robin on 05/07/2018.
 */
@Service
public class AssetServiceImpl implements AssetService {

	private final TeamHasAssetRepository teamHasAssetRepository;
	private final AssetServiceProxy assetServiceProxy;

	public AssetServiceImpl(TeamHasAssetRepository teamHasAssetRepository, AssetServiceProxy assetServiceProxy) {
		this.teamHasAssetRepository = teamHasAssetRepository;
		this.assetServiceProxy = assetServiceProxy;
	}

	@Override
	public void deleteAll(Long teamId) {
		List<TeamHasAssetEntity> allByTeamId = teamHasAssetRepository.findAllByTeamId(teamId);
		teamHasAssetRepository.deleteAll(allByTeamId);
	}

	@Override
	public void saveAll(List<TeamHasAssetEntity> teamHasAssetToSave) {
		teamHasAssetRepository.saveAll(teamHasAssetToSave);
	}

	@Override
	public List<TeamHasAssetEntity> extractAssets(List<Asset> assets, Long idTeam) {
		if (null == assets)
			return new ArrayList<>();

		return assets
				.stream()
				.map(asset -> TeamHasAssetEntity.builder()
						.teamId(idTeam)
						.assetId(asset.getIdAsset())
						.assetMediaId(asset.getIdMedia())
						.build())
				.collect(toList());
	}

	@Override
	public List<AssetEntity> getAssetEntitiesFromIds(List<TeamHasAssetEntity> teamHasAssetEntities) {
		List<AssetEntity> assetEntities = new ArrayList<>();
		for (TeamHasAssetEntity assetEntitiesId : teamHasAssetEntities)
			assetEntities.add(assetServiceProxy.getAssetInfo(assetEntitiesId.getAssetId()));
		return assetEntities;
	}

	@Override
	public List<TeamHasAssetEntity> findAllByTeamId(Long idTeam) {
		return teamHasAssetRepository.findAllByTeamId(idTeam);
	}
}
