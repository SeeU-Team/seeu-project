package com.seeu.darkside.team;

import com.amazonaws.services.s3.AmazonS3;
import com.seeu.darkside.asset.AssetDto;
import com.seeu.darkside.asset.AssetService;
import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.CategoryEntity;
import com.seeu.darkside.category.CategoryService;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.notification.MessagingServiceProxy;
import com.seeu.darkside.rs.dto.*;
import com.seeu.darkside.tag.TagEntity;
import com.seeu.darkside.tag.TagService;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.teamup.TeamUpEntity;
import com.seeu.darkside.teamup.TeamUpService;
import com.seeu.darkside.user.*;
import com.seeu.darkside.utils.GenerateFileUrl;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.seeu.darkside.utils.Constants.TEAM_NOT_FOUND_MSG;
import static java.util.stream.Collectors.toList;

@Service
public class TeamServiceImpl implements TeamService {

	private static final String BUCKET_SOURCE = "seeu-bucket";
	private static final String EXT_PNG = ".png";

	private final TeamRepository teamRepository;
	private final TeamAdapter teamAdapter;
	private final AmazonS3 amazonS3;
	private final UserService userService;
	private final TagService tagService;
	private final AssetService assetService;
	private final CategoryService categoryService;
	private final TeamUpService teamUpService;
	private final MessagingServiceProxy messagingServiceProxy;

	@Autowired
	public TeamServiceImpl(TeamRepository teamRepository,
						   TeamAdapter teamAdapter,
						   UserService userService,
						   TagService tagService,
						   AssetService assetService,
						   CategoryService categoryService,
						   AmazonS3 amazonS3,
						   @Lazy TeamUpService teamUpService,
						   MessagingServiceProxy messagingServiceProxy) {
		this.teamRepository = teamRepository;
		this.teamAdapter = teamAdapter;
		this.amazonS3 = amazonS3;
		this.userService = userService;
		this.tagService = tagService;
		this.assetService = assetService;
		this.categoryService = categoryService;
		this.teamUpService = teamUpService;
		this.messagingServiceProxy = messagingServiceProxy;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamDto> getAllTeams() {
		return teamRepository.findAll()
				.stream()
				.map(teamAdapter::entityToDto)
				.collect(toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamPicture> getAllTeamsPictures() {
		List<TeamEntity> teamEntities = teamRepository.findAll();
		List<TeamPicture> teamPictures = new ArrayList<>();
		for (TeamEntity teamEntity : teamEntities) {
			String pictureKey = teamEntity.getProfilePhotoUrl();
			URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, pictureKey);
			TeamPicture teamPicture = new TeamPicture(teamEntity.getIdTeam(), pictureKey, url.toExternalForm());
			if (teamEntity.getProfilePhotoUrl() != null)
				teamPictures.add(teamPicture);
		}
		return teamPictures;
	}

	@Override
	@Transactional(readOnly = true)
	public TeamDto getTeamDto(Long idTeam) {
		TeamEntity teamEntity = teamRepository
				.findById(idTeam)
				.orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));

		return teamAdapter.entityToDto(teamEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public TeamProfile getTeamProfile(Long idTeam) {
		TeamEntity teamEntity = teamRepository
				.findById(idTeam)
				.orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));

		List<TeamHasUserEntity> userEntities = userService.findAllByTeamId(idTeam);
		List<UserEntity> allMembers = userService.getAllMembersFromIds(userEntities);
		List<TeamHasAssetEntity> assetEntitiesIds = assetService.findAllByTeamId(idTeam);
		List<AssetDto> assetEntities = assetService.getAssetEntitiesFromIds(assetEntitiesIds);
		List<TeamHasCategoryEntity> categoryEntitiesIds = categoryService.findAllByTeamId(idTeam);
		List<CategoryEntity> categoryEntities = categoryService.getCategoryEntitiesFromIds(categoryEntitiesIds);
		List<TeamHasTagEntity> tagEntitiesIds = tagService.findAllByTeamId(idTeam);
		List<TagEntity> tagEntities = tagService.getTagsEntitiesFromIds(tagEntitiesIds);

		URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, teamEntity.getProfilePhotoUrl());

		boolean isMerged = teamUpService.getMergedTeamId(teamEntity.getIdTeam()).isPresent();

		return createTeamProfile(teamEntity, allMembers, assetEntities, categoryEntities, tagEntities, url.toExternalForm(), isMerged);
	}

	@Override
	@Transactional(readOnly = true)
	public TeamHasUser getTeamProfileOfMember(Long memberId) {

		TeamHasUserEntity teamHasUserEntity = userService
				.findByUserId(memberId)
				.orElseThrow(TeammateHasNotTeamException::new);

		TeamProfile teamProfile = getTeamProfile(teamHasUserEntity.getTeamId());

		return TeamHasUser.builder()
				.memberId(teamHasUserEntity.getUserId())
				.team(teamProfile)
				.status(teamHasUserEntity.getStatus())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamProfile> getAllTeamsOfCategoryForTeam(Long categoryId, Long teamId) {
		// TODO: get all teams for category that have not already merged with a team
		// TODO: maybe introduce algorithm to order the result with the more interesting teams first (all teams that liked this one) ??
		final List<TeamUpEntity> allTeamsLikedByCurrentTeam = teamUpService.getAllTeamsLikedByTeam(teamId);


		return categoryService.findAllByCategoryId(categoryId)
				.stream()
				// remove all teams already liked by the current team
				.filter(teamHasCategoryEntity -> !hasAlreadyBeenLiked(teamHasCategoryEntity.getTeamId(), allTeamsLikedByCurrentTeam))
				// remove the current team in the result
				.filter(teamHasCategoryEntity -> !teamId.equals(teamHasCategoryEntity.getTeamId()))
				.map(teamHasCategoryEntity -> getTeamProfile(teamHasCategoryEntity.getTeamId()))
				.collect(toList());
	}

	@Override
	public void checkIfTeamExist(Long idTeam) {
		teamRepository
				.findById(idTeam)
				.orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));
	}

	@Override
	@Transactional
	public TeamProfile addTeammates(AddTeammate teammates) {
		checkIfTeamExist(teammates.getIdTeam());
		List<TeamHasUserEntity> teamHasUserEntities = teammates.getTeammates()
				.stream()
				.map(teammate -> TeamHasUserEntity.builder()
						.teamId(teammates.getIdTeam())
						.userId(teammate.getId())
						.status(TeammateStatus.MEMBER)
						.build())
				.collect(toList());

		userService.saveAll(teamHasUserEntities);

		return getTeamProfile(teammates.getIdTeam());
	}

	@Override
	@Transactional
	public TeamProfile createTeam(TeamCreation teamCreation, String imageBase64) {
		String fileName = savePngInAmazonS3(imageBase64);

		TeamEntity teamToSave = extractTeam(teamCreation, fileName);
		TeamEntity teamSaved = teamRepository.save(teamToSave);
		Long idTeam = teamSaved.getIdTeam();
		List<TeamHasAssetEntity> teamHasAssetToSave = assetService.extractAssets(teamCreation.getAssets(), idTeam);
		List<TeamHasCategoryEntity> teamHasCategoryToSave = categoryService.extractCategories(teamCreation.getCategories(), idTeam);
		List<TeamHasTagEntity> teamHasTagToSave = tagService.extractTags(teamCreation.getTags(), idTeam);
		List<TeamHasUserEntity> teamHasUserToSave = userService.extractUsers(teamCreation.getMembers(), idTeam);

		for (int i = 0; i < teamHasUserToSave.size(); i++) {
			if (i == 0) {
				teamHasUserToSave.get(i).setStatus(TeammateStatus.LEADER);
			} else {
				teamHasUserToSave.get(i).setStatus(TeammateStatus.MEMBER);
			}
		}

		assetService.saveAll(teamHasAssetToSave);
		categoryService.saveAll(teamHasCategoryToSave);
		tagService.saveAll(teamHasTagToSave);
		userService.saveAll(teamHasUserToSave);

		List<AssetDto> assetEntities = assetService.getAssetEntitiesFromIds(teamHasAssetToSave);
		List<CategoryEntity> categoryEntities = categoryService.getCategoryEntitiesFromIds(teamHasCategoryToSave);
		List<TagEntity> tagEntities = tagService.getTagsEntitiesFromIds(teamHasTagToSave);
		List<UserEntity> members = userService.getAllMembersFromIds(teamHasUserToSave);

		updateTeamRegistrationTopics(teamHasUserToSave, members, idTeam);

		URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, fileName);

		return createTeamProfile(teamSaved, members, assetEntities, categoryEntities, tagEntities, url.toExternalForm(), false);
	}

	@Override
	public void updateTeam(TeamUpdate team, String profilePicture) {
		TeamEntity teamToUpdate = teamRepository.getOne(team.getId());
		String fileNameToSave = teamToUpdate.getProfilePhotoUrl();

		if (profilePicture != null)
			fileNameToSave = savePngInAmazonS3(profilePicture);

		Date newDateUpdated = new Date();
		teamToUpdate.setName(team.getName());
		teamToUpdate.setDescription(team.getDescription());
		teamToUpdate.setPlace(team.getPlace());
		teamToUpdate.setProfilePhotoUrl(fileNameToSave);
		teamToUpdate.setUpdated(newDateUpdated);

		Long idTeam = teamToUpdate.getIdTeam();

		teamRepository.save(teamToUpdate);

		List<TeamHasAssetEntity> teamHasAssetToSave = assetService.extractAssets(team.getAssets(), idTeam);
		List<TeamHasCategoryEntity> teamHasCategoryToSave = categoryService.extractCategories(team.getCategories(), idTeam);
		List<TeamHasTagEntity> teamHasTagToSave = tagService.extractTags(team.getTags(), idTeam);
		List<TeamHasUserEntity> teamHasUserFromDto = userService.extractUsers(team.getMembers(), idTeam);
		List<TeamHasUserEntity> members = userService.getAllMembersByTeamId(idTeam);
		userService.updateMembers(teamHasUserFromDto, members);

		assetService.deleteAll(idTeam);
		categoryService.deleteAll(idTeam);
		tagService.deleteAll(idTeam);

		assetService.saveAll(teamHasAssetToSave);
		categoryService.saveAll(teamHasCategoryToSave);
		tagService.saveAll(teamHasTagToSave);
	}

	@Override
	public void deletePictureByIdTeam(Long id) {
		TeamEntity teamEntity = teamRepository
				.findById(id)
				.orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + id));
		teamEntity.setProfilePhotoUrl(null);
		teamRepository.save(teamEntity);
	}

	private String savePngInAmazonS3(String fileInBase64) {
		String fileNameToSave = UUID.randomUUID().getLeastSignificantBits() + EXT_PNG;
		byte[] bytes = Base64.decodeBase64(fileInBase64);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		amazonS3.putObject(BUCKET_SOURCE, fileNameToSave, inputStream, null);

		return fileNameToSave;
	}

	private TeamProfile createTeamProfile(TeamEntity teamEntity, List<UserEntity> members,
										  List<AssetDto> assetEntities, List<CategoryEntity> categoryEntities,
										  List<TagEntity> tagEntities, String profilePhotoUrl,
										  boolean isMerged) {
		return TeamProfile.builder()
				.id(teamEntity.getIdTeam())
				.name(teamEntity.getName())
				.description(teamEntity.getDescription())
				.place(teamEntity.getPlace())
				.profilePhotoUrl(profilePhotoUrl)
				.created(teamEntity.getCreated())
				.updated(teamEntity.getUpdated())
				.members(members)
				.assets(assetEntities)
				.categories(categoryEntities)
				.tags(tagEntities)
				.merged(isMerged)
				.build();
	}

	private TeamEntity extractTeam(TeamCreation teamCreation, String profilePhotoUrl) {
		Date now = new Date();
		return TeamEntity.builder()
				.name(teamCreation.getName())
				.description(teamCreation.getDescription())
				.place(teamCreation.getPlace())
				.profilePhotoUrl(profilePhotoUrl)
				.created(now)
				.updated(now)
				.build();
	}

	/**
	 * Determines if the teamId is present as liked team in the list of TeamUpEntity.
	 *
	 * @param teamId            the team to find
	 * @param alreadyLikedTeams the list of TeamUp
	 * @return true if the team has been liked. Otherwise, return false
	 */
	private boolean hasAlreadyBeenLiked(Long teamId, List<TeamUpEntity> alreadyLikedTeams) {
		for (TeamUpEntity alreadyLikedTeam : alreadyLikedTeams) {
			if (teamId.equals(alreadyLikedTeam.getIdLiked())) {
				return true;
			}
		}

		return false;
	}

	private void updateTeamRegistrationTopics(List<TeamHasUserEntity> teamHasUserToSave, List<UserEntity> users, Long teamId) {
		List<String> registrationTokens = users.stream()
				.map(UserEntity::getAppInstanceId)
				.collect(toList());

		messagingServiceProxy.registerTeamTopic(registrationTokens, teamId);

		// Register the leader to the leader topic
		teamHasUserToSave.stream()
				.filter(teamHasUserEntity -> TeammateStatus.LEADER.equals(teamHasUserEntity.getStatus()))
				.findFirst()
				.ifPresent(teamHasUserEntity -> users.stream()
						.filter(userEntity -> teamHasUserEntity.getUserId().equals(userEntity.getId()))
						.findFirst()
						.ifPresent(userEntity -> messagingServiceProxy.registerLeaderTopic(userEntity.getAppInstanceId(), teamId)));
	}
}
