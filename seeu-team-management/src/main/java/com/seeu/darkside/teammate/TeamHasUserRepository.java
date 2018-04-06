package com.seeu.darkside.teammate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamHasUserRepository extends JpaRepository<TeamHasUserEntity, Long> {
}
