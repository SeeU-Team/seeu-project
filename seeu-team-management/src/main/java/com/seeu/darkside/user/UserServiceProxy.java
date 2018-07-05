package com.seeu.darkside.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("seeu-user-management")
public interface UserServiceProxy {

	@GetMapping("users/{id}")
	UserDto getOneUser(@PathVariable("id") Long id);
}
