package com.seeu.darkside.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.seeu.darkside.config.FirebaseConfig;
import com.seeu.darkside.message.CompleteMessageDto;
import com.seeu.darkside.message.ConversationType;
import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.seeu.darkside.message.ConversationType.TEAM_TO_BEFORE;
import static com.seeu.darkside.message.ConversationType.USER_TO_TEAM;
import static com.seeu.darkside.message.ConversationType.USER_TO_USER;

@Service
public class NotificationServiceImpl implements NotificationService {
	private final Logger logger;

	private final GoogleCredential googleCredential;
	private final FirebaseConfig firebaseConfig;

	@Autowired
	public NotificationServiceImpl(GoogleCredential googleCredential, FirebaseConfig firebaseConfig) {
		logger = Logger.getLogger(this.getClass().getName());
		this.googleCredential = googleCredential;
		this.firebaseConfig = firebaseConfig;
	}

	@Override
	public void sendUserToUserMessageNotification(CompleteMessageDto<User> completeMessageDto, Long userId) {
		List<String> topics = new ArrayList<>(2);
//		topics.add("user-" + completeMessageDto.getOwner().getId());
		topics.add("user-" + userId);

		ConditionFirebaseMessage firebaseMessage = new ConditionFirebaseMessage(
				getData(completeMessageDto, USER_TO_USER),
				Android.getAndroidHighPriority(),
				topics);

		sendFirebaseMessage(firebaseMessage, completeMessageDto.getId());
	}

	@Override
	public void sendTeamMessageNotification(CompleteMessageDto<User> completeMessageDto, Long teamId) {
		TopicFirebaseMessage firebaseMessage = new TopicFirebaseMessage(
				getData(completeMessageDto, USER_TO_TEAM),
				Android.getAndroidHighPriority(),
				"team-" + teamId);

		sendFirebaseMessage(firebaseMessage, completeMessageDto.getId());
	}

	@Override
	public void sendBeforeMessageNotification(CompleteMessageDto<Team> completeMessageDto, Long teamId) {
		List<String> topics = new ArrayList<>(2);
//		topics.add("leader-" + completeMessageDto.getOwner().getId());
		topics.add("leader-" + teamId);

		ConditionFirebaseMessage firebaseMessage = new ConditionFirebaseMessage(
				getData(completeMessageDto, TEAM_TO_BEFORE),
				Android.getAndroidHighPriority(),
				topics);

		sendFirebaseMessage(firebaseMessage, completeMessageDto.getId());
	}

	private Map<String, String> getData(CompleteMessageDto completeMessageDto, ConversationType type) {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonMessage;
		try {
			jsonMessage = objectMapper.writeValueAsString(completeMessageDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			jsonMessage = null;
		}

		Map<String, String> data = new HashMap<>(3);
		data.put("type", "MESSAGE");
		data.put("conversationType", type.name());
		data.put("message", jsonMessage);

		return data;
	}

	private <T extends FirebaseMessage> void sendFirebaseMessage(T firebaseMessage, Long messageId) {
		RestTemplate restTemplate = new RestTemplate();

		RootFirebaseMessage<T> rootFirebaseMessage = RootFirebaseMessage.<T>builder()
				.message(firebaseMessage)
				.build();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());

			HttpEntity<RootFirebaseMessage<T>> httpEntity = new HttpEntity<>(rootFirebaseMessage, headers);
			ResponseEntity<SendNotificationResult> response = restTemplate.postForEntity(firebaseConfig.getUrl(), httpEntity, SendNotificationResult.class);

			if (!HttpStatus.OK.equals(response.getStatusCode())) {
				logger.severe("Error from firebase server while sending new message notification (messageId=" + messageId + ")");
				logger.severe(response.toString());
			}
		} catch (RestClientException | IOException e) {
			logger.severe("Error from firebase server while sending new message notification (messageId=" + messageId + ")");
			e.printStackTrace();
		}
	}

	private String getAccessToken() throws IOException {
		googleCredential.refreshToken();
		return googleCredential.getAccessToken();
	}
}
