package com.seeu.darkside.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient("seeu-message")
public interface MessagingRegistrationServiceProxy {

	@PostMapping("/firebase/registration/team/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	void registerTeamTopic(@RequestBody List<String> registrationTokens, @PathVariable("teamId") Long teamId);

	@PostMapping("/firebase/registration/leader/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	void registerLeaderTopic(@RequestBody String appInstanceId, @PathVariable("teamId") Long teamId);
}
