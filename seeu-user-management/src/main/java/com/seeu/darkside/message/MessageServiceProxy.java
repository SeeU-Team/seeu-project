package com.seeu.darkside.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("seeu-message-management")
public interface MessageServiceProxy {

	@GetMapping("/messages/{userId}/friends")
	List<Long> getFriendsOfUser(@PathVariable("userId") Long userId);
}
