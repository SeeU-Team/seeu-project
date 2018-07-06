package com.seeu.darkside.user;

import java.util.List;
import java.util.Optional;

/**
 * Created by Robin on 05/07/2018.
 */
public interface UserService {
	void saveAll(List<TeamHasUserEntity> teamHasUserToSave);

	void deleteAll(List<TeamHasUserEntity> teamHasUserToSave);

	List<TeamHasUserEntity> getAllMembersByTeamId(Long teamId);

	List<UserEntity> getAllMembersFromIds(List<TeamHasUserEntity> usersEntitiesIds);

	List<TeamHasUserEntity> extractUsers(List<Teammate> members, Long idTeam);

	List<TeamHasUserEntity> findAllByTeamId(Long idTeam);

	Optional<TeamHasUserEntity> findByUserId(Long memberId);

	List<TeamHasUserEntity> updateMembers(List<TeamHasUserEntity> teamHasUserFromDto, List<TeamHasUserEntity> members);
}
