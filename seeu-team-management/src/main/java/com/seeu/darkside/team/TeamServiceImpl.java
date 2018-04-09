package com.seeu.darkside.team;

import com.seeu.darkside.asset.Asset;
import com.seeu.darkside.asset.TeamHasAssetEntity;
import com.seeu.darkside.asset.TeamHasAssetRepository;
import com.seeu.darkside.category.Category;
import com.seeu.darkside.category.TeamHasCategoryEntity;
import com.seeu.darkside.category.TeamHasCategoryRepository;
import com.seeu.darkside.rs.TeamCreation;
import com.seeu.darkside.rs.TeamLike;
import com.seeu.darkside.tag.Tag;
import com.seeu.darkside.tag.TeamHasTagEntity;
import com.seeu.darkside.tag.TeamHasTagRepository;
import com.seeu.darkside.teammate.TeamHasUserEntity;
import com.seeu.darkside.teammate.TeamHasUserRepository;
import com.seeu.darkside.teammate.Teammate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamAdapter teamAdapter;
    private final TeamHasAssetRepository teamHasAssetRepository;
    private final TeamHasCategoryRepository teamHasCategoryRepository;
    private final TeamHasTagRepository teamHasTagRepository;
    private final TeamHasUserRepository teamHasUserRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TeamAdapter teamAdapter, TeamHasAssetRepository teamHasAssetRepository, TeamHasCategoryRepository teamHasCategoryRepository, TeamHasTagRepository teamHasTagRepository, TeamHasUserRepository teamHasUserRepository) {
        this.teamRepository = teamRepository;
        this.teamAdapter = teamAdapter;
        this.teamHasAssetRepository = teamHasAssetRepository;
        this.teamHasCategoryRepository = teamHasCategoryRepository;
        this.teamHasTagRepository = teamHasTagRepository;
        this.teamHasUserRepository = teamHasUserRepository;
    }

    @Override
    public List<TeamDto> getAllTeams() {
        List<TeamDto> teamDtoList = teamRepository.findAll()
                .stream()
                .map((teamEntity) -> {
                    TeamDto teamDto = teamAdapter.entityToDto(teamEntity);
                    return teamDto;
                }).collect(Collectors.toList());
        return teamDtoList;
    }

    @Override
    @Transactional
    public TeamProfile createTeam(TeamCreation teamCreation) {
        TeamProfile teamProfile = null;
        try  {
            TeamEntity teamToSave = extractTeam(teamCreation);
            TeamEntity teamSaved = teamRepository.save(teamToSave);
            Long idTeam = teamSaved.getIdTeam();

            List<TeamHasAssetEntity> teamHasAssetToSave = extractAssets(teamCreation, idTeam);
            List<TeamHasCategoryEntity> teamHasCategoryToSave = extractCategories(teamCreation, idTeam);
            List<TeamHasTagEntity> teamHasTagToSave = extractTags(teamCreation, idTeam);
            List<TeamHasUserEntity> teamHasUserToSave = extractUsers(teamCreation, idTeam);

            for (TeamHasAssetEntity teamHasAssetEntity : teamHasAssetToSave) {
                teamHasAssetRepository.save(teamHasAssetEntity);
            }
            for (TeamHasCategoryEntity teamHasCategoryEntity : teamHasCategoryToSave) {
                teamHasCategoryRepository.save(teamHasCategoryEntity);
            }
            for (TeamHasTagEntity teamHasTagEntity : teamHasTagToSave) {
                teamHasTagRepository.save(teamHasTagEntity);
            }
            for (TeamHasUserEntity teamHasUserEntity : teamHasUserToSave) {
                teamHasUserRepository.save(teamHasUserEntity);
            }

            teamProfile = TeamProfile.builder()
                    .idTeam(idTeam)
                    .name(teamSaved.getName())
                    .description(teamSaved.getDescription())
                    .place(teamSaved.getPlace())
                    .created(teamSaved.getCreated())
                    .updated(teamSaved.getUpdated())
                    .teammateList(teamHasUserToSave)
                    .assets(teamHasAssetToSave)
                    .categories(teamHasCategoryToSave)
                    .tags(teamHasTagToSave)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamProfile;
    }

    @Override
    public void likeTeam(TeamLike teamLike) {

    }

    @Override
    public boolean checkIfTeamExist(Long idTeam) throws TeamNotFoundException {
        TeamEntity oneByIdTeam = teamRepository.findOneByIdTeam(idTeam);
        if (oneByIdTeam == null) {
            throw new TeamNotFoundException("Team Not Found, team_id=" + idTeam);
        }
        return true;
    }

    private List<TeamHasUserEntity> extractUsers(TeamCreation teamCreation, Long idTeam) {
        ArrayList<TeamHasUserEntity> userEntities = new ArrayList<>();
        for (Teammate teammate : teamCreation.getTeammateList()) {
            // todo change status
            TeamHasUserEntity userEntity = TeamHasUserEntity.builder()
                    .teamId(idTeam)
                    .userId(teammate.getIdTeammate())
                    .status("STATUS")
                    .build();

            userEntities.add(userEntity);
        }
        return userEntities;
    }

    private List<TeamHasTagEntity> extractTags(TeamCreation teamCreation, Long idTeam) {
        ArrayList<TeamHasTagEntity> tagEntities = new ArrayList<>();
        for (Tag tag : teamCreation.getTags()) {
            TeamHasTagEntity tagEntity = TeamHasTagEntity.builder()
                    .teamId(idTeam)
                    .tagId(tag.getIdTag())
                    .build();

            tagEntities.add(tagEntity);
        }
        return tagEntities;
    }

    private List<TeamHasCategoryEntity> extractCategories(TeamCreation teamCreation, Long idTeam) {
        ArrayList<TeamHasCategoryEntity> categoryEntities = new ArrayList<>();
        for (Category category : teamCreation.getCategories()) {
            TeamHasCategoryEntity categoryEntity = TeamHasCategoryEntity.builder()
                    .teamId(idTeam)
                    .categoryId(category.getIdCategory()).build();

            categoryEntities.add(categoryEntity);
        }
        return categoryEntities;
    }

    private List<TeamHasAssetEntity> extractAssets(TeamCreation teamCreation, Long idTeam) {
        ArrayList<TeamHasAssetEntity> assetEntities = new ArrayList<>();
        for (Asset asset : teamCreation.getAssets()) {
            TeamHasAssetEntity assetEntity = TeamHasAssetEntity.builder()
                    .teamId(idTeam)
                    .assetId(asset.getIdAsset())
                    .assetMediaId(asset.getIdMedia())
                    .build();

            assetEntities.add(assetEntity);
        }
        return assetEntities;
    }

    private TeamEntity extractTeam(TeamCreation teamCreation) {
        Date now = new Date();

        TeamEntity teamEntity = TeamEntity.builder()
                .name(teamCreation.getName())
                .description(teamCreation.getDescription())
                .place(teamCreation.getPlace())
                .created(now)
                .updated(now)
                .build();

        return teamEntity;
    }
}
