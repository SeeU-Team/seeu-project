package com.seeu.darkside.rs;

import com.seeu.darkside.teamup.MergeEntity;
import com.seeu.darkside.teamup.TeamUpEntity;
import com.seeu.darkside.teamup.TeamUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private TeamUpService teamUpService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus
    public TeamUpEntity likeTeam(@RequestBody TeamLike teamLike) {
        return teamUpService.likeTeam(teamLike);
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
}
