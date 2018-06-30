package com.seeu.darkside.rs;

import com.seeu.darkside.rs.dto.TeamLike;
import com.seeu.darkside.rs.dto.TeamProfile;
import com.seeu.darkside.teamup.MergeEntity;
import com.seeu.darkside.teamup.TeamUpEntity;
import com.seeu.darkside.teamup.TeamUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final TeamUpService teamUpService;

    @Autowired
    public LikeController(TeamUpService teamUpService) {
        this.teamUpService = teamUpService;
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

    @PostMapping
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
}
