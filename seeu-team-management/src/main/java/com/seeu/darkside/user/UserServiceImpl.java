package com.seeu.darkside.user;

import com.seeu.darkside.notification.MessagingServiceProxy;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

/**
 * Created by Robin on 05/07/2018.
 */
@Service
public class UserServiceImpl implements UserService {

	private final TeamHasUserRepository teamHasUserRepository;
	private final UserServiceProxy userServiceProxy;
	private final MessagingServiceProxy messagingServiceProxy;

	public UserServiceImpl(TeamHasUserRepository teamHasUserRepository,
						   UserServiceProxy userServiceProxy,
						   MessagingServiceProxy messagingServiceProxy) {
		this.teamHasUserRepository = teamHasUserRepository;
		this.userServiceProxy = userServiceProxy;
		this.messagingServiceProxy = messagingServiceProxy;
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

		unregisterMembersFromTeamTopic(membersToRemove);
		registerNewMembersToTeamTopic(membersToAdd);

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

	private void unregisterMembersFromTeamTopic(List<TeamHasUserEntity> membersToRemove) {
		if (null == membersToRemove
				|| membersToRemove.isEmpty()) {
			return;
		}

		List<String> unregistrationTokens = getUsersFromMemberIds(membersToRemove)
				.stream()
				.map(UserEntity::getAppInstanceId)
				.collect(toList());

		try {
			messagingServiceProxy.unregisterMembersFromTeamTopic(unregistrationTokens, membersToRemove.get(0).getTeamId());
		} catch (FeignException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.warning("Update team : Remove members from team topic failed");
			e.printStackTrace();
		}
	}

	private void registerNewMembersToTeamTopic(List<TeamHasUserEntity> membersToAdd) {
		if (null == membersToAdd
				|| membersToAdd.isEmpty()) {
			return;
		}

		List<String> registrationTokens = getUsersFromMemberIds(membersToAdd)
				.stream()
				.map(UserEntity::getAppInstanceId)
				.collect(toList());

		try {
			messagingServiceProxy.registerTeamTopic(registrationTokens, membersToAdd.get(0).getTeamId());
		} catch (FeignException e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.warning("Update team : Add members to team topic failed");
			e.printStackTrace();
		}
	}
}
