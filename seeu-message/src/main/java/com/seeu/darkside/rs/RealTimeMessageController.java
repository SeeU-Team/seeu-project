package com.seeu.darkside.rs;

import com.seeu.darkside.message.*;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
public class RealTimeMessageController {

	private final MessageServiceProxy messageService;

	@Autowired
	public RealTimeMessageController(MessageServiceProxy messageService) {
		this.messageService = messageService;
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public MessageDto test(MessageDto message) {
		return message;
	}

	@MessageMapping("/toUser/{userId}")
	@SendTo("/topic/user/{userId}")
	public CompleteMessageDto newUserMessage(@DestinationVariable Long userId, NewMessage newMessage) {
		MessageDto createdMessage = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(userId)
				.content(newMessage.getContent())
				.type(ConversationType.USER_TO_USER)
				.build();

		return createMessageAndHandleErrors(createdMessage);
	}

	@MessageMapping("/toTeam/{teamId}")
	@SendTo("/topic/team/{teamId}")
	public CompleteMessageDto newTeamMessage(@DestinationVariable Long teamId, NewMessage newMessage) {
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
	public CompleteMessageDto newBeforeMessage(@DestinationVariable Long teamId, NewMessage newMessage) {
		MessageDto createdMessage = MessageDto.builder()
				.from(newMessage.getOwner().getId())
				.dest(teamId)
				.content(newMessage.getContent())
				.type(ConversationType.TEAM_TO_BEFORE)
				.build();

		 return createMessageAndHandleErrors(createdMessage);
	}

	private CompleteMessageDto createMessageAndHandleErrors(MessageDto messageDto) throws FeignException, MessageCreationException {
		try {
			return messageService.createMessage(messageDto);
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
