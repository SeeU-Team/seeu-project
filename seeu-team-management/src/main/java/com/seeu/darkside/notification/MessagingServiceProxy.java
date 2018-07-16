package com.seeu.darkside.notification;

import com.seeu.darkside.team.TeamDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient("seeu-message")
public interface MessagingServiceProxy {

	@PostMapping("/firebase/registration/team/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	void registerTeamTopic(@RequestBody List<String> registrationTokens, @PathVariable("teamId") Long teamId);

	@PostMapping("/firebase/registration/leader/{teamId}")
	@ResponseStatus(HttpStatus.CREATED)
	void registerLeaderTopic(@RequestBody String appInstanceId, @PathVariable("teamId") Long teamId);

	@PostMapping("/firebase/unregistration/team/{teamId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void unregisterMembersFromTeamTopic(@RequestBody List<String> unregistrationTokens, @PathVariable("teamId") Long teamId);

	@PostMapping("/firebase/notification/teamUp/{teamId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void sendTeamUpNotification(@RequestBody TeamDto team, @PathVariable("teamId") Long teamId);

	@PostMapping("/firebase/notification/reciprocalTeamUp")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void sendReciprocalTeamUpNotification(@RequestBody TeamsBody teamsBody);

	@PostMapping("/firebase/notification/merge")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void sendMergeNotification(@RequestBody TeamsBody teamsBody);
}
