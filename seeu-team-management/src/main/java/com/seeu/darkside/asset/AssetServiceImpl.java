package com.seeu.darkside.asset;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
						.assetId(asset.getId())
						.assetMediaId(asset.getIdMedia())
						.build())
				.collect(toList());
	}

	@Override
	public List<AssetDto> getAssetEntitiesFromIds(List<TeamHasAssetEntity> teamHasAssetEntities) {
		return teamHasAssetEntities.stream()
				.map(teamHasAssetEntity -> assetServiceProxy.getAssetInfo(teamHasAssetEntity.getAssetId()))
				.map(assetEntity -> AssetDto.builder()
						.id(assetEntity.getId())
						.name(assetEntity.getName())
						.imageDark(assetEntity.getImageDark())
						.imageLight(assetEntity.getImageLight())
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public List<TeamHasAssetEntity> findAllByTeamId(Long idTeam) {
		return teamHasAssetRepository.findAllByTeamId(idTeam);
	}
}
