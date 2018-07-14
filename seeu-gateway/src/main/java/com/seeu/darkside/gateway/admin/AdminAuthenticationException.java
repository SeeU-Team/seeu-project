package com.seeu.darkside.gateway.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AdminAuthenticationException extends RuntimeException {

	public AdminAuthenticationException(String message) {
		super(message);
	}
}
