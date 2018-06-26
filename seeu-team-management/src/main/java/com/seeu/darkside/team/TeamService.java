package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.*;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

    TeamProfile createTeam(TeamCreation teamCreation);

    void checkIfTeamExist(Long idTeam) throws TeamNotFoundException;

    TeamProfile getTeamProfile(Long idTeam);

    TeamProfile addTeammates(AddTeammate teammates);

    TeamHasUser getTeamProfileOfMember(Long memberId);
}
