package com.seeu.darkside.teamup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeRepository extends JpaRepository<MergeEntity, Long> {
}
