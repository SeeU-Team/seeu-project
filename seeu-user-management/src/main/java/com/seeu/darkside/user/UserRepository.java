package com.seeu.darkside.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findOneByEmail(String email);

	Optional<UserEntity> findOneByFacebookId(Long facebookId);
}
