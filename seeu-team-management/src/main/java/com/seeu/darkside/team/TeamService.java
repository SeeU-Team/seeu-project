package com.seeu.darkside.team;

import com.seeu.darkside.rs.dto.*;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TeamService {
	List<TeamDto> getAllTeams();

	@Transactional(readOnly = true)
	List<TeamPicture> getAllTeamsPictures();

	TeamProfile getTeamProfile(Long idTeam);

	TeamHasUser getTeamProfileOfMember(Long memberId);

	List<TeamProfile> getAllTeamsOfCategoryForTeam(Long categoryId, Long teamId);

	void checkIfTeamExist(Long idTeam);

	TeamProfile addTeammates(AddTeammate teammates);

	TeamProfile createTeam(TeamCreation teamCreation, String imageBase64);

	void updateTeam(TeamUpdate team, String profilePicture);
}
