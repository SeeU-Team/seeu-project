package com.seeu.darkside.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.seeu.darkside.rs.CustomFirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Override
	public void registerUserTopic(String appInstanceId, Long userId) {
		List<String> registrationTokens = Collections.singletonList(appInstanceId);
		String topic = "user-" + userId;

		registerTokens(registrationTokens, topic);
	}

	@Override
	public void registerTeamTopic(List<String> registrationTokens, Long teamId) {
		String topic = "team-" + teamId;

		registerTokens(registrationTokens, topic);
	}

	@Override
	public void registerLeaderTopic(String appInstanceId, Long teamId) {
		List<String> registrationTokens = Collections.singletonList(appInstanceId);
		String topic = "leader-" + teamId;

		registerTokens(registrationTokens, topic);
	}

	@Override
	public void unregisterMembersFromTeam(List<String> unregistrationTokens, Long teamId) {
		String topic = "team-" + teamId;

		unregisterTokensFromTopic(unregistrationTokens, topic);
	}

	private void registerTokens(List<String> registrationTokens, String topic) {
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance()
					.subscribeToTopic(registrationTokens, topic);

			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.info(response.getSuccessCount() + " tokens were subscribed successfully");
			logger.severe(response.getFailureCount() + " tokens were not subscribed");
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			throw new CustomFirebaseMessagingException(e);
		}
	}

	private void unregisterTokensFromTopic(List<String> unregistrationTokens, String topic) {
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance()
					.unsubscribeFromTopic(unregistrationTokens, topic);

			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.info(response.getSuccessCount() + " tokens were unsubscribed successfully");
			logger.severe(response.getFailureCount() + " tokens were not unsubscribed");
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			throw new CustomFirebaseMessagingException(e);
		}
	}
}
