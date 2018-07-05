package com.seeu.darkside.team;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("seeu-team-management")
public interface TeamServiceProxy {

	@GetMapping("/teams/{teamId}")
	Team getTeamInfo(@PathVariable("teamId") Long teamId);
}
