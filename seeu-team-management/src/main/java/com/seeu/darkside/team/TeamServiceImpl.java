package com.seeu.darkside.team;

import com.amazonaws.services.s3.AmazonS3;
import com.seeu.darkside.asset.AssetEntity;
import com.seeu.darkside.asset.AssetService;
import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.category.CategoryEntity;
import com.seeu.darkside.category.CategoryService;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.rs.dto.*;
import com.seeu.darkside.tag.TagEntity;
import com.seeu.darkside.tag.TagService;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.user.*;
import com.seeu.darkside.utils.GenerateFileUrl;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
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

	@Autowired
	public TeamServiceImpl(TeamRepository teamRepository,
						   TeamAdapter teamAdapter,
						   UserService userService,
						   TagService tagService,
						   AssetService assetService,
						   CategoryService categoryService,
						   AmazonS3 amazonS3) {
		this.teamRepository = teamRepository;
		this.teamAdapter = teamAdapter;
		this.amazonS3 = amazonS3;
		this.userService = userService;
		this.tagService = tagService;
		this.assetService = assetService;
		this.categoryService = categoryService;
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
	@Transactional
	public TeamProfile createTeam(TeamCreation teamCreation, String imageBase64) {
		TeamProfile teamProfile = null;
		try {
			String fileName = savePngInAmazonS3(imageBase64);

			TeamEntity teamToSave = extractTeam(teamCreation, fileName);
			TeamEntity teamSaved = teamRepository.save(teamToSave);
			Long idTeam = teamSaved.getIdTeam();
			List<TeamHasAssetEntity> teamHasAssetToSave = assetService.extractAssets(teamCreation.getAssets(), idTeam);
			List<TeamHasCategoryEntity> teamHasCategoryToSave = categoryService.extractCategories(teamCreation.getCategories(), idTeam);
			List<TeamHasTagEntity> teamHasTagToSave = tagService.extractTags(teamCreation.getTags(), idTeam);
			List<TeamHasUserEntity> teamHasUserToSave = userService.extractUsers(teamCreation.getMembers(), idTeam);

			assetService.saveAll(teamHasAssetToSave);
			categoryService.saveAll(teamHasCategoryToSave);
			tagService.saveAll(teamHasTagToSave);
			userService.saveAll(teamHasUserToSave);

			for (int i = 0; i < teamHasUserToSave.size(); i++) {
				if (i == 0) {
					teamHasUserToSave.get(i).setStatus(TeammateStatus.LEADER);
				} else {
					teamHasUserToSave.get(i).setStatus(TeammateStatus.MEMBER);
				}
			}

			List<AssetEntity> assetEntities = assetService.getAssetEntitiesFromIds(teamHasAssetToSave);
			List<CategoryEntity> categoryEntities = categoryService.getCategoryEntitiesFromIds(teamHasCategoryToSave);
			List<TagEntity> tagEntities = tagService.getTagsEntitiesFromIds(teamHasTagToSave);
			List<UserEntity> members = userService.getAllMembersFromIds(teamHasUserToSave);

			URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, fileName);

			teamProfile = createTeamProfile(teamSaved, members, assetEntities, categoryEntities, tagEntities, url.toExternalForm());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return teamProfile;
	}

	@Override
	@Transactional(readOnly = true)
	public TeamProfile getTeamProfile(Long idTeam) {
		TeamEntity teamEntity = teamRepository.findById(idTeam).orElseThrow(() ->
				new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));

		List<TeamHasUserEntity> userEntities = userService.findAllByTeamId(idTeam);
		List<UserEntity> allMembers = userService.getAllMembersFromIds(userEntities);
		List<TeamHasAssetEntity> assetEntitiesIds = assetService.findAllByTeamId(idTeam);
		List<AssetEntity> assetEntities = assetService.getAssetEntitiesFromIds(assetEntitiesIds);
		List<TeamHasCategoryEntity> categoryEntitiesIds = categoryService.findAllByTeamId(idTeam);
		List<CategoryEntity> categoryEntities = categoryService.getCategoryEntitiesFromIds(categoryEntitiesIds);
		List<TeamHasTagEntity> tagEntitiesIds = tagService.findAllByTeamId(idTeam);
		List<TagEntity> tagEntities = tagService.getTagsEntitiesFromIds(tagEntitiesIds);

		URL url = GenerateFileUrl.generateUrlFromFile(amazonS3, BUCKET_SOURCE, teamEntity.getProfilePhotoUrl());
		return createTeamProfile(teamEntity, allMembers, assetEntities, categoryEntities, tagEntities, url.toExternalForm());
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeamProfile> getAllTeamsForCategory(Long categoryId) {
		return categoryService.findAllByCategoryId(categoryId)
				.stream()
				.map(teamHasCategoryEntity -> getTeamProfile(teamHasCategoryEntity.getTeamId()))
				.collect(toList());
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
	public void checkIfTeamExist(Long idTeam) {
		teamRepository
				.findById(idTeam)
				.orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));
	}

	@Override
	public void updateTeam(TeamUpdate team, String profilePicture) {
		TeamProfile teamProfile = null;
		try {
			TeamEntity teamToUpdate = teamRepository.getOne(team.getIdTeam());
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
			//List<TeamHasUserEntity> teamHasUserToSave = userService.extractUsers(team.getMembers(), idTeam);

			assetService.deleteAll(idTeam);
			categoryService.deleteAll(idTeam);
			tagService.deleteAll(idTeam);

			assetService.saveAll(teamHasAssetToSave);
			categoryService.saveAll(teamHasCategoryToSave);
			tagService.saveAll(teamHasTagToSave);
			//userService.saveAll(teamHasUserToSave);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String savePngInAmazonS3(String fileInBase64) {
		String fileNameToSave = UUID.randomUUID().getLeastSignificantBits() + EXT_PNG;
		byte[] bytes = Base64.decodeBase64(fileInBase64);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		amazonS3.putObject(BUCKET_SOURCE, fileNameToSave, inputStream, null);

		return fileNameToSave;
	}

	private TeamProfile createTeamProfile(TeamEntity teamEntity, List<UserEntity> members,
										  List<AssetEntity> assetEntities, List<CategoryEntity> categoryEntities,
										  List<TagEntity> tagEntities, String profilePhotoUrl) {
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
}
