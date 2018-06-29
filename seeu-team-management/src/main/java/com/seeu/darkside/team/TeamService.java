package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeamService {
    List<TeamDto> getAllTeams();

	TeamProfile getTeamProfile(Long idTeam);

	TeamHasUser getTeamProfileOfMember(Long memberId);

	List<TeamProfile> getAllTeamsForCategory(Long categoryId);

	TeamProfile createTeam(TeamCreation teamCreation);

    TeamProfile addTeammates(AddTeammate teammates);

	void checkIfTeamExist(Long idTeam) throws TeamNotFoundException;
}
