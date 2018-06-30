package com.seeu.darkside.rs;

import com.seeu.darkside.message.*;
import com.seeu.darkside.team.Team;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
public class RealTimeMessageController {

	private final MessageServiceProxy messageService;
	private final SimpMessageSendingOperations messagingTemplate;

	@Autowired
	public RealTimeMessageController(MessageServiceProxy messageService, SimpMessageSendingOperations messagingTemplate) {
		this.messageService = messageService;
		this.messagingTemplate = messagingTemplate;
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public MessageDto test(MessageDto message) {
		return message;
	}

	@MessageMapping("/toUser/{userId}")
	@SendTo("/topic/user/{userId}")
	public CompleteMessageDto newUserMessage(@DestinationVariable Long userId, NewMessage<User> newMessage) {
		MessageDto createdMessage = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(userId)
				.content(newMessage.getContent())
				.type(ConversationType.USER_TO_USER)
				.build();

		CompleteMessageDto<User> completeMessageDto = createMessageAndHandleErrors(createdMessage);
		sendMessageBackToOwner(completeMessageDto);
		return completeMessageDto;
	}

	@MessageMapping("/toTeam/{teamId}")
	@SendTo("/topic/team/{teamId}")
	public CompleteMessageDto<User> newTeamMessage(@DestinationVariable Long teamId, NewMessage<User> newMessage) {
		MessageDto createdMessage = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(teamId)
				.content(newMessage.getContent())
				.type(ConversationType.USER_TO_TEAM)
				.build();

		return createMessageAndHandleErrors(createdMessage);
	}

	@MessageMapping("/toBefore/{teamId}")
	@SendTo("/topic/leader/{teamId}")
	public CompleteMessageDto<Team> newBeforeMessage(@DestinationVariable Long teamId, NewMessage<Team> newMessage) {
		MessageDto createdMessage = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(teamId)
				.content(newMessage.getContent())
				.type(ConversationType.TEAM_TO_BEFORE)
				.build();

		CompleteMessageDto<Team> completeMessageDto = createMessageAndHandleErrors(createdMessage);
		messagingTemplate.convertAndSend("/topic/leader/" + completeMessageDto.getOwner().getId(), completeMessageDto);

		return completeMessageDto;
	}

	private void sendMessageBackToOwner(CompleteMessageDto<User> completeMessageDto) {
		messagingTemplate.convertAndSend("/topic/user/" + completeMessageDto.getOwner().getId(), completeMessageDto);
	}

	private CompleteMessageDto createMessageAndHandleErrors(MessageDto messageDto) throws FeignException, MessageCreationException {
		try {
			return ConversationType.TEAM_TO_BEFORE.equals(messageDto.getType())
					? messageService.createTeamMessage(messageDto)
					: messageService.createUserMessage(messageDto);
		} catch (FeignException e) {
			if (INTERNAL_SERVER_ERROR.value() == e.status()
					|| BAD_REQUEST.value() == e.status()) {
				throw new MessageCreationException();
			} else {
				throw e;
			}
		}
	}
}
