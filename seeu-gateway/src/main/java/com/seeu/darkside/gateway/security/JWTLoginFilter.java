package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import com.seeu.darkside.gateway.user.UserServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

	private UserServiceProxy userServiceProxy;

	private User user;

	public JWTLoginFilter(String url, AuthenticationManager authManager, UserServiceProxy userServiceProxy) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);

		this.userServiceProxy = userServiceProxy;
		this.user = null;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		// TODO: get token in headers
		final String facebookToken = req.getHeader("access_token");
		setUser(facebookToken);

		if (null == user) {
			logger.info("Authentication failed");
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "L'authentification a échouée.");
			return null;
		}

		return new AnonymousAuthenticationToken(user.getId().toString(), user, Collections.emptyList());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
											FilterChain chain, Authentication auth) throws IOException, ServletException {

		TokenAuthenticationService.addAuthentication(res, user);
	}

	private void setUser(final String token) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			user = restTemplate.getForObject("https://graph.facebook.com/me?fields=id?access_token={token}", User.class, token);

			if (user != null) {
				user = userServiceProxy.getOneByFacebookId(user.getFacebookId());

				// TODO: if user doesn't already exist in DB, add it without any check ?? Or get a signal from caller that this is a first connection ??
			}
		} catch (RestClientException e) {
			user = null;
		}
	}
}
