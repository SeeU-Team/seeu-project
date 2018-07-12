package com.seeu.darkside.rs;

import com.seeu.darkside.message.CompleteMessageDto;
import com.seeu.darkside.message.MessageDto;
import com.seeu.darkside.message.MessageService;
import com.seeu.darkside.message.NewMessage;
import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class RealTimeMessageController {

	private final SimpMessageSendingOperations messagingTemplate;
	private final MessageService messageService;

	@Autowired
	public RealTimeMessageController(SimpMessageSendingOperations messagingTemplate, MessageService messageService) {
		this.messagingTemplate = messagingTemplate;
		this.messageService = messageService;
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public MessageDto test(MessageDto message) {
		return message;
	}

	@MessageMapping("/toUser/{userId}")
	@SendTo("/topic/user/{userId}")
	public CompleteMessageDto<User> newUserMessage(@DestinationVariable Long userId, NewMessage<User> newMessage) {
		CompleteMessageDto<User> completeMessageDto = messageService.createUserMessage(userId, newMessage);

		// Send the message back to its owner
		if (!userId.equals(completeMessageDto.getOwner().getId())) {
			messagingTemplate.convertAndSend("/topic/user/" + completeMessageDto.getOwner().getId(), completeMessageDto);
		}

		return completeMessageDto;
	}

	@MessageMapping("/toTeam/{teamId}")
	@SendTo("/topic/team/{teamId}")
	public CompleteMessageDto<User> newTeamMessage(@DestinationVariable Long teamId, NewMessage<User> newMessage) {
		return messageService.createTeamMessage(teamId, newMessage);
	}

	@MessageMapping("/toBefore/{teamId}")
	@SendTo("/topic/leader/{teamId}")
	public CompleteMessageDto<Team> newBeforeMessage(@DestinationVariable Long teamId, NewMessage<Team> newMessage) {
		CompleteMessageDto<Team> completeMessageDto = messageService.createBeforeMessage(teamId, newMessage);

		// Send the message back to its owner
		messagingTemplate.convertAndSend("/topic/leader/" + completeMessageDto.getOwner().getId(), completeMessageDto);

		return completeMessageDto;
	}
}
