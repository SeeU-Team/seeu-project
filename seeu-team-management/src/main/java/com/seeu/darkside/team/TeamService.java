package com.seeu.darkside.team;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    TeamDto createTeam(TeamDto teamDto);
}
