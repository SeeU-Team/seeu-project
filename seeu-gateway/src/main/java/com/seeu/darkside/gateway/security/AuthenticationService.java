package com.seeu.darkside.gateway.security;

public interface AuthenticationService {

	String getAuthenticationToken(String accessToken) throws FacebookRequestException;
}
