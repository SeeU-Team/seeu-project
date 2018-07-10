package com.seeu.darkside.rs;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamMerge;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.team.TeamNotFoundException;
import com.seeu.darkside.team.TeamService;
import com.seeu.darkside.teamup.MergeEntity;
import com.seeu.darkside.teamup.TeamUpEntity;
import com.seeu.darkside.teamup.TeamUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final TeamUpService teamUpService;
    private final TeamService teamService;

    @Autowired
    public LikeController(TeamUpService teamUpService, TeamService teamService) {
        this.teamUpService = teamUpService;
		this.teamService = teamService;
	}

	/**
	 * DEBUG
	 */
	@GetMapping("/merges")
	public List<MergeEntity> listMerge() {
		return teamUpService.getAllMerge();
	}

	/**
	 * DEBUG
	 */
	@GetMapping()
	public List<TeamUpEntity> listLikes() {
		return teamUpService.getAllLikes();
	}

    @GetMapping(params = "teamId")
    public List<TeamProfile> getAllMutuallyLikedTeams(@RequestParam("teamId") Long teamId) {
		return teamUpService.getAllMutuallyLikedTeams(teamId);
	}

	@GetMapping(value = "/merged", params = "teamId")
	public TeamProfile getMergedTeam(@RequestParam("teamId") Long teamId) {
		return teamUpService
				.getMergedTeamId(teamId)
				.map(teamService::getTeamProfile)
				.orElseThrow(() -> new TeamNotFoundException("No merged team found for the team with id " + teamId));
	}

	@GetMapping(value = "/allMerged", params = "teamId")
	public List<MergeEntity> getAllMergedTeamsByTeam(@RequestParam("teamId") Long teamId) {
		return teamUpService.getAllTeamsAlreadyMergedByTeam(teamId);
	}

    @PostMapping("/like")
    @ResponseBody
    @ResponseStatus(CREATED)
    public TeamUpEntity likeTeam(@RequestBody @Valid TeamLike teamLike,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
			throw new LikeValidationException();
        }

        // TODO: if the user that liked the team is not the leader, send notification to the leader

        return teamUpService.likeTeam(teamLike);
    }

	@PostMapping("/merge")
	@ResponseBody
	@ResponseStatus(CREATED)
	public MergeEntity mergeTeam(@RequestBody @Valid TeamMerge teamMerge,
								 BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new LikeValidationException();
		}

		return teamUpService.mergeTeam(teamMerge);
	}
}
