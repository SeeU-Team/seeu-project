package com.seeu.darkside.team;

import com.seeu.darkside.rs.TeamCreation;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    boolean createTeam(TeamCreation teamCreation);
}
