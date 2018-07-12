package com.seeu.darkside.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by Robin on 05/07/2018.
 */
@Service
public class UserServiceImpl implements UserService {

	private final TeamHasUserRepository teamHasUserRepository;
	private final UserServiceProxy userServiceProxy;

	public UserServiceImpl(TeamHasUserRepository teamHasUserRepository, UserServiceProxy userServiceProxy) {
		this.teamHasUserRepository = teamHasUserRepository;
		this.userServiceProxy = userServiceProxy;
	}


	@Override
	public void saveAll(List<TeamHasUserEntity> teamHasUserToSave) {
		teamHasUserRepository.saveAll(teamHasUserToSave);
	}

	@Override
	public void deleteAll(List<TeamHasUserEntity> teamHasUserToSave) {
		teamHasUserRepository.deleteAll(teamHasUserToSave);
	}

	@Override
	public List<TeamHasUserEntity> getAllMembersByTeamId(Long teamId) {
		List<TeamHasUserEntity> allByTeamId = teamHasUserRepository.findAllByTeamId(teamId);
		return allByTeamId;
	}

	@Override
	public List<UserEntity> getAllMembersFromIds(List<TeamHasUserEntity> usersEntitiesIds) {
		List<UserEntity> userEntities = getUsersFromMemberIds(usersEntitiesIds);
		return userEntities;
	}

	@Override
	public List<TeamHasUserEntity> extractUsers(List<Teammate> members, Long idTeam) {
		if (null == members) {
			return new ArrayList<>();
		}

		return members
				.stream()
				.map(teammate -> TeamHasUserEntity.builder()
						.teamId(idTeam)
						.userId(teammate.getId())
						.build())
				.collect(toList());
	}

	@Override
	public List<TeamHasUserEntity> findAllByTeamId(Long idTeam) {
		return teamHasUserRepository.findAllByTeamId(idTeam);
	}

	@Override
	public Optional<TeamHasUserEntity> findByUserId(Long memberId) {
		return teamHasUserRepository.findByUserId(memberId);
	}

	@Override
	public List<TeamHasUserEntity> updateMembers(List<TeamHasUserEntity> teamHasUserFromDto, List<TeamHasUserEntity> members) {
		List<TeamHasUserEntity> membersToAdd = new ArrayList<>();
		List<TeamHasUserEntity> membersToRemove = new ArrayList<>();
		for (TeamHasUserEntity member : members) {
			if (!teamHasUserFromDto.contains(member))
				membersToRemove.add(member);
		}
		for (TeamHasUserEntity teamHasUserEntity : teamHasUserFromDto) {
			if (!members.contains(teamHasUserEntity))
				membersToAdd.add(teamHasUserEntity);
		}

		teamHasUserRepository.deleteAll(membersToRemove);
		teamHasUserRepository.saveAll(membersToAdd);

		// TODO: remove registration for team topic for membersToRemove
		// TODO: add registration for team topic for membersToAdd

		return membersToAdd;
	}

	private List<UserEntity> getUsersFromMemberIds(List<TeamHasUserEntity> usersEntitiesIds) {
		List<UserEntity> userEntities = new ArrayList<>();
		for (TeamHasUserEntity usersEntitiesId : usersEntitiesIds) {
			UserDto oneUser = userServiceProxy.getOneUser(usersEntitiesId.getUserId());
			UserEntity userEntity = UserEntity.builder()
					.id(oneUser.getId())
					.facebookId(oneUser.getFacebookId())
					.appInstanceId(oneUser.getAppInstanceId())
					.name(oneUser.getName())
					.gender(oneUser.getGender())
					.email(oneUser.getEmail())
					.description(oneUser.getDescription())
					.profilePhotoUrl(oneUser.getProfilePhotoUrl())
					.status(usersEntitiesId.getStatus())
					.created(oneUser.getCreated())
					.updated(oneUser.getUpdated())
					.build();
			userEntities.add(userEntity);
		}
		return userEntities;
	}
}
