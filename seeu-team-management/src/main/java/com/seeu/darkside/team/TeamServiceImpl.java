package com.seeu.darkside.team;

import com.amazonaws.services.s3.AmazonS3;
import com.seeu.darkside.asset.AssetEntity;
import com.seeu.darkside.asset.AssetServiceProxy;
import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.asset.TeamHasAssetRepository;
import com.seeu.darkside.category.CategoryEntity;
import com.seeu.darkside.category.CategoryServiceProxy;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.category.TeamHasCategoryRepository;
import com.seeu.darkside.rs.dto.AddTeammate;
import com.seeu.darkside.rs.dto.TeamCreation;
import com.seeu.darkside.rs.dto.TeamHasUser;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.tag.*;
import com.seeu.darkside.teammate.TeamHasUserEntity;
import com.seeu.darkside.teammate.TeamHasUserRepository;
import com.seeu.darkside.teammate.TeammateHasNotTeamException;
import com.seeu.darkside.teammate.TeammateStatus;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.seeu.darkside.utils.Constants.TEAM_NOT_FOUND_MSG;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String BUCKET_SOURCE = "seeu-bucket";

    private final TeamRepository teamRepository;
    private final TeamAdapter teamAdapter;
    private final TeamHasAssetRepository teamHasAssetRepository;
    private final TeamHasCategoryRepository teamHasCategoryRepository;
    private final TeamHasTagRepository teamHasTagRepository;
    private final TeamHasUserRepository teamHasUserRepository;
    private final AssetServiceProxy assetServiceProxy;
    private final CategoryServiceProxy categoryServiceProxy;
    private final TagServiceProxy tagServiceProxy;
    private final AmazonS3 amazonS3;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TeamAdapter teamAdapter, TeamHasAssetRepository teamHasAssetRepository, TeamHasCategoryRepository teamHasCategoryRepository, TeamHasTagRepository teamHasTagRepository, TeamHasUserRepository teamHasUserRepository, AssetServiceProxy assetServiceProxy, CategoryServiceProxy categoryServiceProxy, TagServiceProxy tagServiceProxy, AmazonS3 amazonS3) {
        this.teamRepository = teamRepository;
        this.teamAdapter = teamAdapter;
        this.teamHasAssetRepository = teamHasAssetRepository;
        this.teamHasCategoryRepository = teamHasCategoryRepository;
        this.teamHasTagRepository = teamHasTagRepository;
        this.teamHasUserRepository = teamHasUserRepository;
        this.assetServiceProxy = assetServiceProxy;
        this.categoryServiceProxy = categoryServiceProxy;
        this.tagServiceProxy = tagServiceProxy;
        this.amazonS3 = amazonS3;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamAdapter::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeamProfile createTeam(TeamCreation teamCreation, String imageBase64, String fileName) {
        TeamProfile teamProfile = null;
        try {
            TeamEntity teamToSave = extractTeam(teamCreation);
            TeamEntity teamSaved = teamRepository.save(teamToSave);
            Long idTeam = teamSaved.getIdTeam();
            List<TeamHasAssetEntity> teamHasAssetToSave = extractAssets(teamCreation, idTeam);
            List<TeamHasCategoryEntity> teamHasCategoryToSave = extractCategories(teamCreation, idTeam);
            List<TeamHasTagEntity> teamHasTagToSave = extractTags(teamCreation, idTeam);
            List<TeamHasUserEntity> teamHasUserToSave = extractUsers(teamCreation, idTeam);


            // TODO USER
            teamHasAssetRepository.saveAll(teamHasAssetToSave);
            teamHasCategoryRepository.saveAll(teamHasCategoryToSave);
            teamHasTagRepository.saveAll(teamHasTagToSave);
            teamHasUserRepository.saveAll(teamHasUserToSave);

            for (int i = 0; i < teamHasUserToSave.size(); i++) {
                if (i == 0) {
                    teamHasUserToSave.get(i).setStatus(TeammateStatus.LEADER);
                } else {
                    teamHasUserToSave.get(i).setStatus(TeammateStatus.MEMBER);
                }
            }

            // todo Save image base 64, save le nom du fichier dans la bdd
            String fileExtension = FilenameUtils.getExtension(fileName);
            String fileNameToSave = teamToSave.getName() + "-" + teamToSave.getIdTeam() + "." + fileExtension;
            byte[] bytes = Base64.decodeBase64(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            amazonS3.putObject(BUCKET_SOURCE, fileNameToSave, inputStream, null);

            List<AssetEntity> assetEntities = getAssetEntitiesFromIds(teamHasAssetToSave);
            List<CategoryEntity> categoryEntities = getCategoryEntitiesFromIds(teamHasCategoryToSave);
            List<TagEntity> tagEntities = getTagsEntitiesFromIds(teamHasTagToSave);

            teamProfile = createTeamProfile(teamSaved, teamHasUserToSave, assetEntities, categoryEntities, tagEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamProfile;
    }

    @Override
    public void checkIfTeamExist(Long idTeam) throws TeamNotFoundException {
        teamRepository
                .findById(idTeam)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamProfile getTeamProfile(Long idTeam) {
        TeamEntity teamEntity = teamRepository.findById(idTeam).orElseThrow(() ->
                new TeamNotFoundException(TEAM_NOT_FOUND_MSG + idTeam));


        List<TeamHasUserEntity> userEntities = teamHasUserRepository.findAllByTeamId(idTeam);

        List<TeamHasAssetEntity> assetEntitiesIds = teamHasAssetRepository.findAllByTeamId(idTeam);
        List<AssetEntity> assetEntities = getAssetEntitiesFromIds(assetEntitiesIds);

        List<TeamHasCategoryEntity> categoryEntitiesIds = teamHasCategoryRepository.findAllByTeamId(idTeam);
        List<CategoryEntity> categoryEntities = getCategoryEntitiesFromIds(categoryEntitiesIds);

        List<TeamHasTagEntity> tagEntitiesIds = teamHasTagRepository.findAllByTeamId(idTeam);
        List<TagEntity> tagEntities = getTagsEntitiesFromIds(tagEntitiesIds);

        return createTeamProfile(teamEntity, userEntities, assetEntities, categoryEntities, tagEntities);
    }


    @Override
    @Transactional
    public TeamProfile addTeammates(AddTeammate teammates) {
        checkIfTeamExist(teammates.getIdTeam());

        List<TeamHasUserEntity> teamHasUserEntities = teammates.getTeammates()
                .stream()
                .map(teammate -> TeamHasUserEntity.builder()
                        .teamId(teammates.getIdTeam())
                        .userId(teammate.getIdTeammate())
                        .status(TeammateStatus.MEMBER)
                        .build())
                .collect(Collectors.toList());

        teamHasUserRepository.saveAll(teamHasUserEntities);

        return getTeamProfile(teammates.getIdTeam());
    }

    @Override
    @Transactional(readOnly = true)
    public TeamHasUser getTeamProfileOfMember(Long memberId) {

        TeamHasUserEntity teamHasUserEntity = teamHasUserRepository
                .findByUserId(memberId)
                .orElseThrow(TeammateHasNotTeamException::new);

        TeamProfile teamProfile = getTeamProfile(teamHasUserEntity.getTeamId());

        return TeamHasUser.builder()
                .memberId(teamHasUserEntity.getUserId())
                .team(teamProfile)
                .status(teamHasUserEntity.getStatus())
                .build();
    }

    private TeamProfile createTeamProfile(TeamEntity teamEntity, List<TeamHasUserEntity> userEntities, List<AssetEntity> assetEntities, List<CategoryEntity> categoryEntities, List<TagEntity> tagEntities) {
        return TeamProfile.builder()
                .idTeam(teamEntity.getIdTeam())
                .name(teamEntity.getName())
                .description(teamEntity.getDescription())
                .place(teamEntity.getPlace())
                .created(teamEntity.getCreated())
                .updated(teamEntity.getUpdated())
                .teammateList(userEntities)
                .assets(assetEntities)
                .categories(categoryEntities)
                .tags(tagEntities)
                .build();
    }

    private List<TeamHasUserEntity> extractUsers(TeamCreation teamCreation, Long idTeam) {
        if (null == teamCreation.getTeammateList()) {
            return new ArrayList<>();
        }

        return teamCreation.getTeammateList()
                .stream()
                .map(teammate -> TeamHasUserEntity.builder()
                        .teamId(idTeam)
                        .userId(teammate.getIdTeammate())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TeamHasTagEntity> extractTags(TeamCreation teamCreation, Long idTeam) {
        if (null == teamCreation.getTags()) {
            return new ArrayList<>();
        }
        List<TeamHasTagEntity> tagEntities = new ArrayList<>();
        for (Tag tag : teamCreation.getTags()) {
            TagEntity newTagIfNotExist = tagServiceProxy.createNewTagIfNotExist(new TagDTO(null, tag.getTagName()));
            TeamHasTagEntity teamHasTagEntity = TeamHasTagEntity.builder().teamId(idTeam).tagId(newTagIfNotExist.getIdTag()).build();
            tagEntities.add(teamHasTagEntity);
        }
        return tagEntities;
    }

    private List<TeamHasCategoryEntity> extractCategories(TeamCreation teamCreation, Long idTeam) {
        if (null == teamCreation.getCategories()) {
            return new ArrayList<>();
        }

        return teamCreation.getCategories()
                .stream()
                .map(category -> TeamHasCategoryEntity.builder()
                        .teamId(idTeam)
                        .categoryId(category.getIdCategory())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TeamHasAssetEntity> extractAssets(TeamCreation teamCreation, Long idTeam) {
        if (null == teamCreation.getAssets()) {
            return new ArrayList<>();
        }

        return teamCreation.getAssets()
                .stream()
                .map(asset -> TeamHasAssetEntity.builder()
                        .teamId(idTeam)
                        .assetId(asset.getIdAsset())
                        .assetMediaId(asset.getIdMedia())
                        .build())
                .collect(Collectors.toList());
    }

    private TeamEntity extractTeam(TeamCreation teamCreation) {
        Date now = new Date();
        return TeamEntity.builder()
                .name(teamCreation.getName())
                .description(teamCreation.getDescription())
                .place(teamCreation.getPlace())
                .created(now)
                .updated(now)
                .build();
    }

    private List<AssetEntity> getAssetEntitiesFromIds(List<TeamHasAssetEntity> teamHasAssetEntities) {
        List<AssetEntity> assetEntities = new ArrayList<>();
        for (TeamHasAssetEntity assetEntitiesId : teamHasAssetEntities)
            assetEntities.add(assetServiceProxy.getAssetInfo(assetEntitiesId.getAssetId()));
        return assetEntities;
    }


    private List<CategoryEntity> getCategoryEntitiesFromIds(List<TeamHasCategoryEntity> categoryEntitiesIds) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for (TeamHasCategoryEntity categoryEntityId : categoryEntitiesIds)
            categoryEntities.add(categoryServiceProxy.getCategoryInfo(categoryEntityId.getCategoryId()));
        return categoryEntities;
    }

    private List<TagEntity> getTagsEntitiesFromIds(List<TeamHasTagEntity> tagEntitiesIds) {
        List<TagEntity> tagEntities = new ArrayList<>();
        for (TeamHasTagEntity tagEntity : tagEntitiesIds)
            tagEntities.add(tagServiceProxy.getTagInfo(tagEntity.getTagId()));
        return tagEntities;
    }
}
