package com.seeu.darkside.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamHasAssetRepository extends JpaRepository<TeamHasAssetEntity, Long> {
}
