package com.seeu.darkside.gateway.security;

import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;

public class TokenAuthenticationUtil {

	@Value("${token.config.secret}")
	private static String secret;

	public static final String TOKEN_PREFIX = "Bearer";

	public static byte[] getSecretKey() {
		byte[] secretBytes;

		try {
			secretBytes = secret.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			secretBytes = secret.getBytes();
		}

		return secretBytes;
	}
}
