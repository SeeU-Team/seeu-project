package com.seeu.darkside.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient("seeu-message-management")
public interface MessageServiceProxy {

	@PostMapping(value = "/messages")
	CompleteMessageDto createMessage(@RequestBody @Valid MessageDto messageDto);
}
