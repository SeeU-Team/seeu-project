package com.seeu.darkside.user;

import com.seeu.darkside.rs.dto.TeamCreation;

import java.util.List;
import java.util.Optional;

/**
 * Created by Robin on 05/07/2018.
 */
public interface UserService {
	void saveAll(List<TeamHasUserEntity> teamHasUserToSave);

	List<UserEntity> getAllMembersFromIds(List<TeamHasUserEntity> usersEntitiesIds);

	List<TeamHasUserEntity> extractUsers(TeamCreation teamCreation, Long idTeam);

	List<TeamHasUserEntity> findAllByTeamId(Long idTeam);

	Optional<TeamHasUserEntity> findByUserId(Long memberId);
}
