package com.seeu.darkside.team;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seeu-team-management")
public interface TeamServiceProxy {

	@GetMapping(value = "/teams", params = {"memberId"})
	TeamHasUser getTeamOfMember(@RequestParam("memberId") Long memberId);
}
