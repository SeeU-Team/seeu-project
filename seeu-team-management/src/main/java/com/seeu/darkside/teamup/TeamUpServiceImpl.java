package com.seeu.darkside.teamup;

import com.seeu.darkside.notification.MessagingServiceProxy;
import com.seeu.darkside.notification.TeamsBody;
import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamMerge;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.team.TeamDto;
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
    private final MessagingServiceProxy messagingServiceProxy;

    @Autowired
    public TeamUpServiceImpl(TeamUpRepository teamUpRepository,
							 MergeRepository mergeRepository,
							 TeamService teamService,
							 MessagingServiceProxy messagingServiceProxy) {
        this.teamUpRepository = teamUpRepository;
        this.mergeRepository = mergeRepository;
        this.teamService = teamService;
		this.messagingServiceProxy = messagingServiceProxy;
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
	@Transactional(readOnly = true)
	public Optional<Long> getMergedTeamId(Long teamId) {
		List<MergeEntity> mergeEntities = mergeRepository.findAllByIdFirstOrIdSecond(teamId);

		for (int i = 0; i < mergeEntities.size() - 1; i++) {
			for (int j = i + 1; j < mergeEntities.size(); j++) {
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

	@Override
	@Transactional(readOnly = true)
	public List<TeamUpEntity> getAllTeamsLikedByTeam(Long teamId) {
    	return teamUpRepository.findAllByIdLike(teamId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MergeEntity> getAllTeamsAlreadyMergedByTeam(Long teamId) {
		return mergeRepository.findAllByIdFirst(teamId);
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

		TeamDto firstTeam = teamService.getTeamDto(teamUpEntity.getIdLike());

		if (isReciprocalLike(teamLike)) {
			// Send notif to all members of each team of the mutually liked
			TeamDto secondTeam = teamService.getTeamDto(teamUpEntity.getIdLiked());

			TeamsBody teamsBody = TeamsBody.builder()
					.firstTeam(firstTeam)
					.secondTeam(secondTeam)
					.build();
			messagingServiceProxy.sendReciprocalTeamUpNotification(teamsBody);
		} else {
			// Send notif to all members of the liked team
			messagingServiceProxy.sendTeamUpNotification(firstTeam, teamUpEntity.getIdLiked());
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
			// Send notif to members of each team that they have mutually merged
			TeamDto firstTeam = teamService.getTeamDto(mergeEntity.getIdFirst());
			TeamDto secondTeam = teamService.getTeamDto(mergeEntity.getIdSecond());
			TeamsBody teamsBody = TeamsBody.builder()
					.firstTeam(firstTeam)
					.secondTeam(secondTeam)
					.build();

			messagingServiceProxy.sendMergeNotification(teamsBody);
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
		boolean hasFirstTeamMerged = getMergedTeamId(teamMerge.getIdFirst()).isPresent();
		boolean hasSecondTeamMerged = getMergedTeamId(teamMerge.getIdSecond()).isPresent();

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
}
