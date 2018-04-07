package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.seeu.darkside.gateway.security.TokenAuthenticationUtil.TOKEN_PREFIX;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	protected JWTAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
		final String token = httpServletRequest.getHeader(AUTHORIZATION);
		Authentication authentication = null;

		if (token != null) {
			try {
				// parse the token.
				final Claims claims = Jwts.parser()
						.setSigningKey(TokenAuthenticationUtil.getSecretKey())
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody();

				final String userId = claims.getSubject();
				final User user = claims.get("user", User.class);

				authentication = new AnonymousAuthenticationToken(userId, user, emptyList());

				// TODO: Renew expiration time of token ???
			} catch (Exception e) {
				e.printStackTrace();
				authentication = null;
			}
		}

		// authentication = new AnonymousAuthenticationToken("0", "0", Arrays.asList(new SimpleGrantedAuthority("USER")));
		return authentication;
	}
}
