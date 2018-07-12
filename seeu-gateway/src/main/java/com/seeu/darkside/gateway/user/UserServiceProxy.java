package com.seeu.darkside.gateway.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient("seeu-user-management")
public interface UserServiceProxy {

	@GetMapping(value = "/users", params = "facebookId")
	User getOneByFacebookId(@RequestParam(value="facebookId") Long id);

	@PostMapping(value = "/users")
	User createNewUser(@RequestBody User userDto);

	@PutMapping("/users/{id}/appInstanceId")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void updateAppInstanceId(@PathVariable("id") Long id, @RequestBody String appInstanceId);
}
