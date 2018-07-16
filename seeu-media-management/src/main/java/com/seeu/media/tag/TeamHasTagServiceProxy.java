package com.seeu.media.tag;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("seeu-team-management")
public interface TeamHasTagServiceProxy {

	@DeleteMapping("/teams/tag/{id}")
	void deleteTagTeam(@PathVariable("id") Long id);
}
