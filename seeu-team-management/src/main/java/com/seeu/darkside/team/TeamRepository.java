package com.seeu.darkside.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    TeamEntity save(TeamEntity teamEntity);

    TeamEntity findOneByIdTeam(Long idTeam);
}
