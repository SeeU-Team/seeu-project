package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.AddTeammate;
import com.seeu.darkside.rs.dto.TeamCreation;
import com.seeu.darkside.rs.dto.TeamHasUser;
import com.seeu.darkside.rs.dto.TeamProfile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TeamService {
	List<TeamDto> getAllTeams();

	TeamProfile createTeam(TeamCreation teamCreation, String imageBase64);

	TeamProfile getTeamProfile(Long idTeam);

	TeamHasUser getTeamProfileOfMember(Long memberId);

	List<TeamProfile> getAllTeamsForCategory(Long categoryId);

	//TeamProfile createTeam(TeamCreation teamCreation, String profilePhotoUrl);

	TeamProfile addTeammates(AddTeammate teammates);

	void checkIfTeamExist(Long idTeam);

	void updateTeam(TeamCreation team, String profilePicture);
}
