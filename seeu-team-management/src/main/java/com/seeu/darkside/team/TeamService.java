package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.AddTeammate;
import com.seeu.darkside.rs.dto.TeamCreation;
import com.seeu.darkside.rs.dto.TeamHasUser;
import com.seeu.darkside.rs.dto.TeamProfile;

import java.util.List;

public interface TeamService {
	List<TeamDto> getAllTeams();

	TeamProfile createTeam(TeamCreation teamCreation, String imageBase64, String fileName);

	TeamProfile getTeamProfile(Long idTeam);

	TeamHasUser getTeamProfileOfMember(Long memberId);

	List<TeamProfile> getAllTeamsForCategory(Long categoryId);

	//TeamProfile createTeam(TeamCreation teamCreation, String profilePhotoUrl);

	TeamProfile addTeammates(AddTeammate teammates);

	void checkIfTeamExist(Long idTeam);
}
