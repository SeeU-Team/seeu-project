package com.seeu.darkside.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamHasTagRepository extends JpaRepository<TeamHasTagEntity, Long> {
    List<TeamHasTagEntity> findAllByTeamId(Long idTeam);
}
