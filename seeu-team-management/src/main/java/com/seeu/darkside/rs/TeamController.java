package com.seeu.darkside.rs;

import com.seeu.darkside.asset.Asset;
import com.seeu.darkside.asset.AssetEntity;
import com.seeu.darkside.asset.AssetServiceProxy;
import com.seeu.darkside.category.Category;
import com.seeu.darkside.rs.dto.*;
import com.seeu.darkside.tag.Tag;
import com.seeu.darkside.team.TeamDto;
import com.seeu.darkside.team.TeamService;
import com.seeu.darkside.teammate.Teammate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponseWrapper;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public TeamProfile createNewTeam(@RequestBody @Valid TeamCreationRoot teamCreationRoot, BindingResult bindingResult) {

    	if (bindingResult.hasErrors()) {
			throw new TeamValidationException();
		}

		// Save the base64 picture to AWS...

        return teamService.createTeam(teamCreationRoot.getTeam());
    }

    @GetMapping("/{teamId}")
    public TeamProfile getTeamInfo(@PathVariable("teamId") Long teamId) {
        return teamService.getTeamProfile(teamId);
    }

    @GetMapping(params = {"memberId"})
    public TeamHasUser getTeamOfMember(@RequestParam("memberId") Long memberId) {
		return teamService.getTeamProfileOfMember(memberId);
    }

    @PostMapping("/addTeammates")
    public TeamProfile addTeammates(@RequestBody AddTeammate teammates) {
        return teamService.addTeammates(teammates);
    }

    /**
     * DEBUG
     * @return
     */
    @GetMapping("/list")
    public List<TeamDto> listTeams() {
        return teamService.getAllTeams();
    }

    /**
     * DEBUG
     * @return
     */
    @GetMapping("/cre")
    public TeamCreation getTeamCreationJson() {
        String name = "Team Name";
        String description = "Team Description";
        String place = "Paris";
        ArrayList<Teammate> teammateList = new ArrayList<>();
        Teammate teammate = new Teammate(23123L);
        teammateList.add(teammate);
        ArrayList<Asset> assets = new ArrayList<>();
        Asset asset = new Asset(1212L, 3434L);
        Asset asset2 = new Asset(2121L, 4343L);
        assets.add(asset);
        assets.add(asset2);
        ArrayList<Category> categories = new ArrayList<>();
        Category category = new Category(555L);
        categories.add(category);
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag = new Tag(666L);
        tags.add(tag);
        TeamCreation teamCreation = new TeamCreation(name, description, place, teammateList, assets, categories, tags);
        return teamCreation;
    }
}
