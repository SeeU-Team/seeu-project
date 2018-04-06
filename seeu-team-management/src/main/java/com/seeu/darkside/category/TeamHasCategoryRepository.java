package com.seeu.darkside.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamHasCategoryRepository extends JpaRepository<TeamHasCategoryEntity, Long> {
}
