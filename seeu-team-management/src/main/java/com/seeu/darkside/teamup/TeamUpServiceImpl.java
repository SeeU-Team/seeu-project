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

        if (isReciprocalLike(teamLike)) {
            // TODO: send notif via notification micro service to the leader of each team
            mergeTeams(teamLike);
        }

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

        return mergeRepository.save(newMerge);
    }

    private boolean isReciprocalLike(TeamLike teamLike) {
        return teamUpRepository.findIfTeamIsLiked(teamLike.getIdLike(), teamLike.getIdLiked()).isPresent();
    }
}
