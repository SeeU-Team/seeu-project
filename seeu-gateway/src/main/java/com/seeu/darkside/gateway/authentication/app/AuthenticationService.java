package com.seeu.darkside.gateway.authentication.app;

import org.springframework.tuple.Tuple;

public interface AuthenticationService {

	Tuple getAuthenticationToken(LoginBody loginBody) throws FacebookRequestException;
}
