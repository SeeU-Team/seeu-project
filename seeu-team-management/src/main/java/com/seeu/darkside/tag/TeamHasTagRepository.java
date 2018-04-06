package com.seeu.darkside.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamHasTagRepository extends JpaRepository<TeamHasTagEntity, Long> {
}
