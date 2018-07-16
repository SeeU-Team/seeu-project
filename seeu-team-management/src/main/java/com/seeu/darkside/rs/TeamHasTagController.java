package com.seeu.darkside.rs;

import com.seeu.darkside.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams/tags")
public class TeamHasTagController {

	private final TagService tagService;

	@Autowired
	public TeamHasTagController(TagService tagService) {
		this.tagService = tagService;
	}

	@DeleteMapping("/{id}")
	public void deleteTagTeam(@PathVariable("id") Long id) {
		tagService.deleteTeamHasTag(id);
	}
}
