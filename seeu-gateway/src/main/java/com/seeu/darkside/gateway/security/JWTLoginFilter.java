package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import com.seeu.darkside.gateway.user.UserServiceProxy;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
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
import java.util.Date;

import static com.seeu.darkside.gateway.security.TokenAuthenticationUtil.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

	private UserServiceProxy userServiceProxy;

	@Value("${token.config.expiration-time}")
	public static long expirationTime;

	public JWTLoginFilter(String url, AuthenticationManager authManager, UserServiceProxy userServiceProxy) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);

		this.userServiceProxy = userServiceProxy;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {

		// TODO: get token in headers
		final String facebookToken = req.getHeader("access_token");
		final User user = getUser(facebookToken);

		final Authentication authentication = new AnonymousAuthenticationToken(user.getId().toString(), user, Collections.emptyList());
		return getAuthenticationManager().authenticate(authentication);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
											FilterChain chain, Authentication auth) throws IOException, ServletException {
		final String jwt = Jwts.builder()
				.claim("user", auth.getPrincipal())
				.setSubject(((User) auth.getPrincipal()).getId().toString())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(SignatureAlgorithm.HS512, TokenAuthenticationUtil.getSecretKey())
				.compact();

		res.addHeader(AUTHORIZATION, TOKEN_PREFIX + " " + jwt);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);

		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
	}

	private User getUser(final String token) {
		if (null == token) {
			throw new AccessTokenRequiredException("No token provided", null);
		}

		User user;

		try {
			final RestTemplate restTemplate = new RestTemplate();
			user = restTemplate.getForObject("https://graph.facebook.com/me?fields=id?access_token={token}", User.class, token);

			if (null == user) {
				throw new UsernameNotFoundException("The user information could not been retrieve from Facebook");
			}

			user = userServiceProxy.getOneByFacebookId(user.getFacebookId());

			if (null == user) {
				// TODO: if user doesn't already exist in DB, add it without any check ?? Or get a signal from caller that this is a first connection ??
				throw new UsernameNotFoundException("The user doesn't exist yet in the database");
			}
		} catch (RestClientException e) {
			throw new InternalAuthenticationServiceException("An error occurred while trying to get user information", e);
		}

		return user;
	}
}
