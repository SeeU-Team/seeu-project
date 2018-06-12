package com.seeu.darkside.gateway.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiAuthenticationToken extends AbstractAuthenticationToken {

	private Object principal;

	public ApiAuthenticationToken(Long idUser, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);

		this.principal = idUser;
		this.setAuthenticated(true);
	}

	public static ApiAuthenticationToken getDeniedAuthentication() {
		final ApiAuthenticationToken authentication = new ApiAuthenticationToken(null, null);
		authentication.setAuthenticated(false);

		return authentication;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}
