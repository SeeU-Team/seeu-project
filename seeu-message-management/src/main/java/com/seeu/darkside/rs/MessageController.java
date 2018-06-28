package com.seeu.darkside.rs;

import com.seeu.darkside.message.CompleteMessageDto;
import com.seeu.darkside.message.MessageDto;
import com.seeu.darkside.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/messages")
public class MessageController {

	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping(params = {"firstUserId", "secondUserId"})
	public List<CompleteMessageDto> getAllMessagesBetweenUsers(@RequestParam("firstUserId") Long firstUserId,
															   @RequestParam("secondUserId") Long secondUserId) {
		return messageService.getAllMessagesForUserToUser(firstUserId, secondUserId);
	}

	@GetMapping(params = "teamId")
	public List<CompleteMessageDto> getAllMessagesForTeam(@RequestParam("teamId") Long teamId) {
		return messageService.getAllMessagesForTeam(teamId);
	}

	@GetMapping(params = {"firstTeamId", "secondTeamId"})
	public List<CompleteMessageDto> getAllMessagesBetweenTeams(@RequestParam("firstTeamId") Long firstTeamId,
															   @RequestParam("secondTeamId") Long secondTeamId) {
		return messageService.getAllMessagesForTeamToBefore(firstTeamId, secondTeamId);
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public CompleteMessageDto createMessage(@RequestBody @Valid MessageDto messageDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new MessageValidationException();
		}

		return messageService.createMessage(messageDto);
	}
}
