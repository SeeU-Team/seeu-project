package com.seeu.darkside.rs;

import com.seeu.darkside.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams/categories")
public class TeamHasCategoryController {

	private final CategoryService categoryService;

	@Autowired
	public TeamHasCategoryController(CategoryService categoryService) {
		this.categoryService= categoryService;
	}

	@DeleteMapping("/{id}")
	public void deleteCategoryTeam(@PathVariable("id") Long id) {
		categoryService.deleteTeamHasCategory(id);
	}
}
