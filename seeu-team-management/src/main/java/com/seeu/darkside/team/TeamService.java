package com.seeu.darkside.team;

import com.seeu.darkside.rs.TeamCreation;
import com.seeu.darkside.rs.TeamLike;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    TeamProfile createTeam(TeamCreation teamCreation);

    void likeTeam(TeamLike teamLike);

    boolean checkIfTeamExist(Long idLike) throws TeamNotFoundException;
}
