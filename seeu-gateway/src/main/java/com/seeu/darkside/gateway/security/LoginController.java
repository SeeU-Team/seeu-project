package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.tuple.Tuple;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.seeu.darkside.gateway.security.TokenAuthenticationUtil.TOKEN_PREFIX;

@RestController
public class LoginController {

	private final AuthenticationService authenticationService;

	@Autowired
	public LoginController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(value = "/login")
	public ResponseEntity<User> login(@RequestBody @Valid LoginBody loginBody) throws FacebookRequestException {
		if (null == loginBody.getAccessToken()) {
			throw new IllegalArgumentException("An access token must be provided");
		}

		final Tuple tuple = authenticationService.getAuthenticationToken(loginBody);
		final String token = tuple.getString("TOKEN");
		final User user = (User) tuple.getValue("USER");
		final HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + token);

		return new ResponseEntity<>(user, headers, HttpStatus.OK);
	}
}
