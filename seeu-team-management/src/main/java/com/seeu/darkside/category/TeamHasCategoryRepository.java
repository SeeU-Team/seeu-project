package com.seeu.darkside.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamHasCategoryRepository extends JpaRepository<TeamHasCategoryEntity, Long> {
    List<TeamHasCategoryEntity> findAllByTeamId(Long idTeam);
}
