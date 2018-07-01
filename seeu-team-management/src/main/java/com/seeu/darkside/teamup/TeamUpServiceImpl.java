package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamMerge;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        TeamUpEntity teamUpEntity = TeamUpEntity.builder()
                .idLike(teamLike.getIdLike())
                .idLiked(teamLike.getIdLiked())
                .build();

        teamUpEntity = teamUpRepository.save(teamUpEntity);

		if (isReciprocalLike(teamLike)) {
			// TODO: send notif via notification micro service to the leader of each team
		}

        return teamUpEntity;
    }

    @Override
	@Transactional
	public MergeEntity mergeTeam(TeamMerge teamMerge) {
		teamService.checkIfTeamExist(teamMerge.getIdFirst());
		teamService.checkIfTeamExist(teamMerge.getIdSecond());

		checkMutuallyLike(teamMerge);
		checkNotAlreadyMerge(teamMerge);

		MergeEntity mergeEntity = MergeEntity.builder()
				.idFirst(teamMerge.getIdFirst())
				.idSecond(teamMerge.getIdSecond())
				.build();

		mergeEntity = mergeRepository.save(mergeEntity);

		if (isReciprocalMerge(teamMerge)) {
			// TODO: Send notif to teams that they have mutually merged.
			// TODO: Set merge status to yes for the teams ???
		}

		return mergeEntity;
	}

	/**
	 * Check if the teams have mutually liked before merge them.
	 * @param teamMerge the merge to do
	 */
	private void checkMutuallyLike(TeamMerge teamMerge) {
    	boolean first = teamUpRepository.findIfTeamIsLiked(teamMerge.getIdFirst(), teamMerge.getIdSecond()).isPresent();
    	boolean second = teamUpRepository.findIfTeamIsLiked(teamMerge.getIdFirst(), teamMerge.getIdSecond()).isPresent();

    	if(!first || !second) {
    		throw new ImpossibleMergeException();
		}
	}

	/**
	 * Check that the teams have not already merged with another team.
	 * If one of them have already merged, throw a {@link ImpossibleMergeException}.
	 * @param teamMerge the merge to do
	 */
	private void checkNotAlreadyMerge(TeamMerge teamMerge) {
		List<MergeEntity> firstList = mergeRepository.findAllByIdFirstOrIdSecond(teamMerge.getIdFirst());
		boolean hasFirstTeamMerged = getMergedTeamId(firstList, teamMerge.getIdFirst()).isPresent();

		List<MergeEntity> secondList = mergeRepository.findAllByIdFirstOrIdSecond(teamMerge.getIdSecond());
		boolean hasSecondTeamMerged = getMergedTeamId(secondList, teamMerge.getIdSecond()).isPresent();

		if (hasFirstTeamMerged
				|| hasSecondTeamMerged) {
			throw new ImpossibleMergeException();
		}
	}

    private boolean isReciprocalLike(TeamLike teamLike) {
        return teamUpRepository.findIfTeamIsLiked(teamLike.getIdLike(), teamLike.getIdLiked()).isPresent();
    }

    private boolean isReciprocalMerge(TeamMerge teamMerge) {
    	return mergeRepository.findIfTeamIsMerged(teamMerge.getIdFirst(), teamMerge.getIdSecond()).isPresent();
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

					Long otherTeamId = first.getIdLike().equals(teamId)
							? first.getIdLiked()
							: first.getIdLike();

					otherTeamIds.add(otherTeamId);
				}
			}
		}

    	return otherTeamIds
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}

	private Optional<Long> getMergedTeamId(List<MergeEntity> mergeEntities, Long teamId) {

		for (int i = 0; i < mergeEntities.size() - 1; i++) {
			for (int j = i + 1; i < mergeEntities.size(); j++) {
				MergeEntity firstMerge = mergeEntities.get(i);
				MergeEntity secondMerge = mergeEntities.get(j);

				// Mutually merged
				if (firstMerge.getIdFirst().equals(secondMerge.getIdSecond())
						&& firstMerge.getIdSecond().equals(secondMerge.getIdFirst())) {

					Long otherTeamId = firstMerge.getIdFirst().equals(teamId)
							? firstMerge.getIdSecond()
							: firstMerge.getIdFirst();

					return Optional.of(otherTeamId);
				}
			}
		}

		return Optional.empty();
	}
}
