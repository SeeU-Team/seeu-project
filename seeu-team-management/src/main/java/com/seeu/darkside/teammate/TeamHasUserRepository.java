package com.seeu.darkside.teammate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamHasUserRepository extends JpaRepository<TeamHasUserEntity, Long> {
    List<TeamHasUserEntity> findAllByTeamId(Long teamId);
}
