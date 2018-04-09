package com.seeu.darkside.teamup;

import com.seeu.darkside.rs.TeamLike;

import java.util.List;

public interface TeamUpService {
    TeamUpEntity likeTeam(TeamLike teamLike);

    List<MergeEntity> getAllMerge();

    List<TeamUpEntity> getAllLikes();
}
