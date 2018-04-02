package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import static java.util.Collections.emptyList;

public class TokenAuthenticationService {

	static final long EXPIRATIONTIME = 86_400_000; // one day in millisecond
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	private static byte[] getSecretKey() {
		byte[] secret;

		try {
			secret = SECRET.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			secret = SECRET.getBytes();
		}

		return secret;
	}

	/**
	 * Add the token in the header response
	 * @param res
	 * @param user
	 */
	public static void addAuthentication(HttpServletResponse res, User user) {

		final String JWT = Jwts.builder()
				.claim("user", user)
				.setSubject(user.getId().toString())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, getSecretKey())
				.compact();

		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	/**
	 * Get the user from a request
	 * @param request
	 * @return
	 */
	public static Authentication getAuthentication(HttpServletRequest request) {
		Authentication authentication = null;
		final String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			try {
				// parse the token.
				final String userId = Jwts.parser()
						.setSigningKey(getSecretKey())
						.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
						.getBody()
						.getSubject();

				authentication = new AnonymousAuthenticationToken(userId, null, emptyList());

				// TODO: Renew expiration time of token ???
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return authentication;
	}
}
