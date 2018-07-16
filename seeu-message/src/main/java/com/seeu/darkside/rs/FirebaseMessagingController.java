package com.seeu.darkside.rs;

import com.seeu.darkside.notification.NotificationService;
import com.seeu.darkside.notification.RegistrationService;
import com.seeu.darkside.rs.dto.TeamsBody;
import com.seeu.darkside.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firebase")
public class FirebaseMessagingController {

	private final RegistrationService registrationService;
	private final NotificationService notificationService;

	@Autowired
	public FirebaseMessagingController(RegistrationService registrationService,
									   NotificationService notificationService) {
		this.registrationService = registrationService;
		this.notificationService = notificationService;
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

	@PostMapping("/notification/teamUp/{teamId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sendTeamUpNotification(@RequestBody Team team, @PathVariable("teamId") Long teamId) {
		notificationService.sendTeamUpNotification(team, teamId);
	}

	@PostMapping("/notification/reciprocalTeamUp")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sendReciprocalTeamUpNotification(@RequestBody TeamsBody teamsBody) {
		notificationService.sendReciprocalTeamUpNotification(teamsBody.getFirstTeam(), teamsBody.getSecondTeam());
	}

	@PostMapping("/notification/merge")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void sendMergeNotification(@RequestBody TeamsBody teamsBody) {
		notificationService.sendMergeNotification(teamsBody.getFirstTeam(), teamsBody.getSecondTeam());
	}
}
