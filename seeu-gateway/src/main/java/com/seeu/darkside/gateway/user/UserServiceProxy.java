package com.seeu.darkside.gateway.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("seeu-user-management")
public interface UserServiceProxy {

	@GetMapping(value = "/users", params = "facebookId")
	User getOneByFacebookId(@RequestParam(value="facebookId") Long id);
}
