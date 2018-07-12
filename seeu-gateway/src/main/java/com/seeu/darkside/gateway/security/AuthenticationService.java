package com.seeu.darkside.gateway.security;

import org.springframework.tuple.Tuple;

public interface AuthenticationService {

	Tuple getAuthenticationToken(LoginBody loginBody) throws FacebookRequestException;
}
