package com.seeu.media.asset;

import com.seeu.media.rs.dto.AssetDTO;

import java.util.List;

public interface AssetService {
    AssetEntity createAsset(AssetDTO assetyDTO);

    AssetEntity getAsset(Long assetId);

    List<AssetEntity> getAllAssets();
}
