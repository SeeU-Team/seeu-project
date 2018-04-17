package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class TokenAuthenticationUtil {

	@Value("${token.config.secret}")
	private static String secret;

	@Value("${token.config.expiration-time}")
	private static long expirationTime = 86_400_000;

	public static final String TOKEN_PREFIX = "Bearer";

	public static byte[] getSecretKey() {
		byte[] secretBytes;

		try {
			secret = (null == secret) ? "ThisIsASecret" : secret;
			secretBytes = secret.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			secretBytes = secret.getBytes();
		}

		return secretBytes;
	}

	public static String generateToken(final User user) {
		return Jwts.builder()
//				.claim("user", user)
				.setSubject(user.getIdUser().toString())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(SignatureAlgorithm.HS512, TokenAuthenticationUtil.getSecretKey())
				.compact();
	}
}
