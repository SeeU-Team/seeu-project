package com.seeu.darkside.facebook;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FacebookRequestException extends RuntimeException {

	public FacebookRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public FacebookRequestException(String message) {
		super(message);
	}
}
