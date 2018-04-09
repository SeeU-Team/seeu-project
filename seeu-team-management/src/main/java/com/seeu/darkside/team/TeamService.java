package com.seeu.darkside.team;

import com.seeu.darkside.rs.TeamCreation;
import com.seeu.darkside.rs.TeamLike;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    boolean createTeam(TeamCreation teamCreation);

    void likeTeam(TeamLike teamLike);
}
