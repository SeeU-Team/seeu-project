package com.seeu.darkside.teammate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamHasUserRepository extends JpaRepository<TeamHasUserEntity, Long> {
    List<TeamHasUserEntity> findAllByTeamId(Long teamId);

    Optional<TeamHasUserEntity> findByUserId(Long userId);
}
