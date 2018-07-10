package com.seeu.darkside.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MergeRepository extends JpaRepository<MergeEntity, Long> {

	@Query("select m from MergeEntity m where m.idSecond = ?1 and m.idFirst = ?2")
	Optional<MergeEntity> findIfTeamIsMerged(Long idMerge, Long idMerged);

	@Query("select m from MergeEntity m where m.idFirst = ?1 or m.idSecond = ?1")
	List<MergeEntity> findAllByIdFirstOrIdSecond(Long idTeam);

	List<MergeEntity> findAllByIdFirst(Long idFirst);
}
