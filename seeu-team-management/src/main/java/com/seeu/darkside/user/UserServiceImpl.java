package com.seeu.darkside.user;

import com.seeu.darkside.rs.dto.TeamCreation;
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
	public List<UserEntity> getAllMembersFromIds(List<TeamHasUserEntity> usersEntitiesIds) {
		List<UserEntity> userEntities = new ArrayList<>();
		for (TeamHasUserEntity usersEntitiesId : usersEntitiesIds) {
			UserDto oneUser = userServiceProxy.getOneUser(usersEntitiesId.getUserId());
			UserEntity userEntity = UserEntity.builder()
					.id(oneUser.getId())
					.facebookId(oneUser.getFacebookId())
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

	@Override
	public List<TeamHasUserEntity> extractUsers(TeamCreation teamCreation, Long idTeam) {
		if (null == teamCreation.getMembers()) {
			return new ArrayList<>();
		}

		return teamCreation.getMembers()
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
}
