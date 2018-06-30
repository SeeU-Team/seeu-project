package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	@Transactional(readOnly = true)
    public List<MergeEntity> getAllMerge() {
        return mergeRepository.findAll();
    }

    @Override
	@Transactional(readOnly = true)
    public List<TeamUpEntity> getAllLikes() {
        return teamUpRepository.findAll();
    }

    @Override
	@Transactional(readOnly = true)
    public List<TeamProfile> getAllMutuallyLikedTeams(Long teamId) {
    	List<TeamUpEntity> teamUpEntities = teamUpRepository.findAllByIdLikeOrIdLiked(teamId);

    	return getMutuallyLikedTeamIds(teamUpEntities, teamId)
				.stream()
				.map(teamService::getTeamProfile)
				.collect(Collectors.toList());
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

    private List<Long> getMutuallyLikedTeamIds(List<TeamUpEntity> teamUpEntities, Long teamId) {
    	List<Long> otherTeamIds = new ArrayList<>();

		for (int i = 0; i < teamUpEntities.size() - 1; i++) {
			for (int j = i + 1; j < teamUpEntities.size(); j++) {
				TeamUpEntity first = teamUpEntities.get(i);
				TeamUpEntity second = teamUpEntities.get(j);

				// Mutually liked
				if (first.getIdLike().equals(second.getIdLiked())
						&& first.getIdLiked().equals(second.getIdLike())) {

					Long otherTeamId = first.getIdLike().equals(teamId) ? first.getIdLiked() : first.getIdLike();
					otherTeamIds.add(otherTeamId);
				}
			}
		}

    	return otherTeamIds
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}
}
