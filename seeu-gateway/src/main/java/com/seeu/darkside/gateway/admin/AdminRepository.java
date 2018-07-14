package com.seeu.darkside.gateway.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

	Optional<AdminEntity> findOneByEmail(String email);
}
