package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamMerge;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.team.TeamNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TeamUpService {

    List<MergeEntity> getAllMerge();

    List<TeamUpEntity> getAllLikes();

	List<TeamProfile> getAllMutuallyLikedTeams(Long teamId);

	Optional<Long> getMergedTeamId(Long teamId);

	TeamUpEntity likeTeam(TeamLike teamLike) throws TeamNotFoundException;

	MergeEntity mergeTeam(TeamMerge teamMerge);
}
