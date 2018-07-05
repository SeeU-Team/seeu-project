package com.seeu.darkside.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeammateHasNotTeamException extends RuntimeException {
}
