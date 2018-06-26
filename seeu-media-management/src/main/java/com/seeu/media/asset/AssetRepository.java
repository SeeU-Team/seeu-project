package com.seeu.media.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    AssetEntity findOneByIdAsset(Long assetId);
}
