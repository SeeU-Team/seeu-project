package com.seeu.darkside.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.seeu.darkside.gateway.security.TokenAuthenticationUtil.TOKEN_PREFIX;

@RestController
public class LoginController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping(value = "/login")
	public ResponseEntity login(@RequestBody String accessToken) throws FacebookRequestException {
		if (null == accessToken) {
			throw new IllegalArgumentException("An access token must be provided");
		}

		final String token = authenticationService.getAuthenticationToken(accessToken);
		final HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + token);

		return new ResponseEntity(headers, HttpStatus.OK);
	}
}
