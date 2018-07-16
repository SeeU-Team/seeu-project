package com.seeu.darkside.rs;

import com.seeu.darkside.rs.dto.*;
import com.seeu.darkside.team.TeamDto;
import com.seeu.darkside.team.TeamPicture;
import com.seeu.darkside.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

	private final TeamService teamService;

	@Autowired
	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	/**
	 * DEBUG
	 *
	 * @return
	 */
	@GetMapping
	public List<TeamDto> listTeams() {
		return teamService.getAllTeams();
	}

	@GetMapping("/{teamId}")
	public TeamProfile getTeamInfo(@PathVariable("teamId") Long teamId) {
		return teamService.getTeamProfile(teamId);
	}

	@GetMapping(params = {"memberId"})
	public TeamHasUser getTeamOfMember(@RequestParam("memberId") Long memberId) {
		return teamService.getTeamProfileOfMember(memberId);
	}

	@GetMapping(params = {"categoryId", "teamId"})
	public List<TeamProfile> getAllTeamsOfCategoryForTeam(@RequestParam("categoryId") Long categoryId,
														  @RequestParam("teamId") Long teamId) {
		return teamService.getAllTeamsOfCategoryForTeam(categoryId, teamId);
	}

	@GetMapping("/pictures")
	public List<TeamPicture> getTeamsPictures() {
		return teamService.getAllTeamsPictures();
	}

	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public TeamProfile createNewTeam(@RequestBody @Valid TeamCreationRoot teamCreationRoot,
									 BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new TeamValidationException();
		}

		return teamService.createTeam(teamCreationRoot.getTeam(), teamCreationRoot.getProfilePicture());
	}

	@PostMapping("/addTeammates")
	public TeamProfile addTeammates(@RequestBody AddTeammate teammates) {
		return teamService.addTeammates(teammates);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateTeam(@RequestBody TeamUpdateRoot teamUpdateRoot) {
		teamService.updateTeam(teamUpdateRoot.getTeam(), teamUpdateRoot.getProfilePicture());
	}

	@DeleteMapping("/pictures/{id}")
	public void deletePictureTeam(@PathVariable("id") Long id) {
		teamService.deletePictureByIdTeam(id);
	}
}
