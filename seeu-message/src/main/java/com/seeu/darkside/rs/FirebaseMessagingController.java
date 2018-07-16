package com.seeu.darkside.rs;

import com.seeu.darkside.notification.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firebase")
public class FirebaseMessagingController {

	private final RegistrationService registrationService;

	@Autowired
	public FirebaseMessagingController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@PostMapping("/registration/user/{userId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerUserTopic(@RequestBody String appInstanceId, @PathVariable("userId") Long userId) {
		registrationService.registerUserTopic(appInstanceId, userId);
	}

	@PostMapping("/registration/team/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerTeamTopic(@RequestBody List<String> registrationTokens, @PathVariable("teamId") Long teamId) {
		registrationService.registerTeamTopic(registrationTokens, teamId);
	}

	@PostMapping("/registration/leader/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerLeaderTopic(@RequestBody String appInstanceId, @PathVariable("teamId") Long teamId) {
		registrationService.registerLeaderTopic(appInstanceId, teamId);
	}

	@PostMapping("/unregistration/team/{teamId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void unregisterMembersToTopic(@RequestBody List<String> unregistrationTokens, @PathVariable("teamId") Long teamId) {
		registrationService.unregisterMembersFromTeam(unregistrationTokens, teamId);
	}
}
