package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamUpServiceImpl implements TeamUpService {

    private final TeamUpRepository teamUpRepository;
    private final MergeRepository mergeRepository;
    private final TeamService teamService;

    @Autowired
    public TeamUpServiceImpl(TeamUpRepository teamUpRepository, MergeRepository mergeRepository, TeamService teamService) {
        this.teamUpRepository = teamUpRepository;
        this.mergeRepository = mergeRepository;
        this.teamService = teamService;
    }

    @Override
    @Transactional
    public TeamUpEntity likeTeam(TeamLike teamLike) {

        teamService.checkIfTeamExist(teamLike.getIdLike());
        teamService.checkIfTeamExist(teamLike.getIdLiked());

        if (teamsNeedToBeMerge(teamLike))
            mergeTeams(teamLike);

        TeamUpEntity teamUpEntityToSave = TeamUpEntity.builder()
                .idLike(teamLike.getIdLike())
                .idLiked(teamLike.getIdLiked())
                .build();

        return teamUpRepository.save(teamUpEntityToSave);
    }

    @Override
    public List<MergeEntity> getAllMerge() {
        return mergeRepository.findAll();
    }

    @Override
    public List<TeamUpEntity> getAllLikes() {
        return teamUpRepository.findAll();
    }

    private MergeEntity mergeTeams(TeamLike teamLike) {
        MergeEntity newMerge = MergeEntity.builder()
                .idFirst(teamLike.getIdLiked())
                .idSecond(teamLike.getIdLike())
                .build();

        MergeEntity savedEntity = mergeRepository.save(newMerge);
        return savedEntity;
    }

    private boolean teamsNeedToBeMerge(TeamLike teamLike) {
        TeamUpEntity teamIsLiked = teamUpRepository.findIfTeamIsLiked(teamLike.getIdLike(), teamLike.getIdLiked());
        if (teamIsLiked != null)
            return true;
        else
            return false;
    }
}
