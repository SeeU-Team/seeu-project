package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.TeamCreation;
import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamProfile;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    TeamProfile createTeam(TeamCreation teamCreation);

    void likeTeam(TeamLike teamLike);

    boolean checkIfTeamExist(Long idLike) throws TeamNotFoundException;

    TeamProfile getTeamProfile(Long idTeam);
}
