package com.seeu.darkside.message;

import com.seeu.darkside.notification.NotificationService;
import com.seeu.darkside.rs.MessageCreationException;
import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageServiceProxy messageService;
	private final NotificationService notificationService;

	@Autowired
	public MessageServiceImpl(MessageServiceProxy messageService, NotificationService notificationService) {
		this.messageService = messageService;
		this.notificationService = notificationService;
	}

	@Override
	public CompleteMessageDto<User> createUserMessage(Long userId, NewMessage<User> newMessage) {
		MessageDto messageDto = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(userId)
				.content(newMessage.getContent())
				.type(ConversationType.USER_TO_USER)
				.build();

		CompleteMessageDto<User> result = null;

		try {
			result = messageService.createUserMessage(messageDto);
			notificationService.sendUserToUserMessageNotification(result, userId);
		} catch (FeignException e) {
			handleMessageServiceProxyException(e);
		}

		return result;
	}

	@Override
	public CompleteMessageDto<User> createTeamMessage(Long teamId, NewMessage<User> newMessage) {
		MessageDto messageDto = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(teamId)
				.content(newMessage.getContent())
				.type(ConversationType.USER_TO_TEAM)
				.build();

		CompleteMessageDto<User> result = null;

		try {
			result = messageService.createUserMessage(messageDto);
			notificationService.sendTeamMessageNotification(result, teamId);
		} catch (FeignException e) {
			handleMessageServiceProxyException(e);
		}

		return result;
	}

	@Override
	public CompleteMessageDto<Team> createBeforeMessage(Long teamId, NewMessage<Team> newMessage) {
		MessageDto messageDto = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(teamId)
				.content(newMessage.getContent())
				.type(ConversationType.TEAM_TO_BEFORE)
				.build();

		CompleteMessageDto<Team> result = null;

		try {
			result = messageService.createTeamMessage(messageDto);
			notificationService.sendBeforeMessageNotification(result, teamId);
		} catch (FeignException e) {
			handleMessageServiceProxyException(e);
		}

		return result;
	}

	private void handleMessageServiceProxyException(FeignException e) {
		if (INTERNAL_SERVER_ERROR.value() == e.status()
				|| BAD_REQUEST.value() == e.status()) {
			throw new MessageCreationException();
		} else {
			throw e;
		}
	}
}
