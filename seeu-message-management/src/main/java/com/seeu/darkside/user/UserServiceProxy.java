package com.seeu.darkside.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("seeu-user-management")
public interface UserServiceProxy {

	@GetMapping("/users/{id}")
	User getOneUser(@PathVariable("id") Long id);
}
