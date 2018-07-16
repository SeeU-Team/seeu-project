package com.seeu.darkside.notification;

import java.util.List;

public interface RegistrationService {

	void registerUserTopic(String appInstanceId, Long userId);

	void registerTeamTopic(List<String> registrationTokens, Long teamId);

	void registerLeaderTopic(String appInstanceId, Long teamId);

	void unregisterMembersFromTeam(List<String> unregistrationTokens, Long teamId);
}
