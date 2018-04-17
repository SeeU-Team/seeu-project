package com.seeu.darkside.gateway.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FacebookRequestException extends Exception {

	public FacebookRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public FacebookRequestException(String message) {
		super(message);
	}
}
