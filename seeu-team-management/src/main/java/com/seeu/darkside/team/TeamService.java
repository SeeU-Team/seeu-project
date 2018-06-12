package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.AddTeammate;
import com.seeu.darkside.rs.dto.TeamCreation;
import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamProfile;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    TeamProfile createTeam(TeamCreation teamCreation);

    void checkIfTeamExist(Long idTeam) throws TeamNotFoundException;

    TeamProfile getTeamProfile(Long idTeam);

    TeamProfile addTeammates(AddTeammate teammates);
}
