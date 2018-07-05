package com.seeu.darkside.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamHasAssetRepository extends JpaRepository<TeamHasAssetEntity, Long> {
	List<TeamHasAssetEntity> findAllByTeamId(Long idTeam);

	void deleteAllByTeamId(Long teamId);
}
