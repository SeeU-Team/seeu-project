package com.seeu.darkside.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamUpRepository extends JpaRepository<TeamUpEntity, Long> {

    @Query("select t from TeamUpEntity t where t.idLiked = ?1 and t.idLike = ?2")
    Optional<TeamUpEntity> findIfTeamIsLiked(Long idLike, Long idLiked);

    @Query("select t from TeamUpEntity t where t.idLike = ?1 or t.idLiked = ?1")
    List<TeamUpEntity> findAllByIdLikeOrIdLiked(Long id);

	// Override the query because the spring jpa is lost with the field IdLike (the sql 'like' word).
	@Query("select t from TeamUpEntity t where t.idLike = ?1")
    List<TeamUpEntity> findAllByIdLike(Long idLike);
}
