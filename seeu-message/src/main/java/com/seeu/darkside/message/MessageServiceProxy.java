package com.seeu.darkside.message;

import com.seeu.darkside.team.Team;
import com.seeu.darkside.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient("seeu-message-management")
public interface MessageServiceProxy {

	@PostMapping(value = "/messages")
	CompleteMessageDto<User> createUserMessage(@RequestBody @Valid MessageDto messageDto);

	@PostMapping(value = "/messages")
	CompleteMessageDto<Team> createTeamMessage(@RequestBody @Valid MessageDto messageDto);
}
