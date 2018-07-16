package com.seeu.media.category;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("seeu-team-management")
public interface TeamHasCategoryServiceProxy {

	@DeleteMapping("/teams/categories/{id}")
	void deleteCategoryTeam(@PathVariable("id") Long id);
}
