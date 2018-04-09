package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.TeamLike;
import com.seeu.darkside.team.TeamNotFoundException;

import java.util.List;

public interface TeamUpService {
    TeamUpEntity likeTeam(TeamLike teamLike) throws TeamNotFoundException;

    List<MergeEntity> getAllMerge();

    List<TeamUpEntity> getAllLikes();
}
