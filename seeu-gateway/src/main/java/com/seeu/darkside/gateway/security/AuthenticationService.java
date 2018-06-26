package com.seeu.darkside.gateway.security;

import org.springframework.tuple.Tuple;

public interface AuthenticationService {

	Tuple getAuthenticationToken(String accessToken) throws FacebookRequestException;
}
