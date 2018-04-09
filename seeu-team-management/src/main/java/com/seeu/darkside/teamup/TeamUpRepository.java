package com.seeu.darkside.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamUpRepository extends JpaRepository<TeamUpEntity, Long> {

    @Query("select t from TeamUpEntity t where t.idLiked = ?1 and t.idLike = ?2")
    TeamUpEntity findIfTeamIsLiked(Long idLike, Long idLiked);
}
