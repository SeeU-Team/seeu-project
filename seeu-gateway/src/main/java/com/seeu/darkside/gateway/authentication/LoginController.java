package com.seeu.darkside.gateway.authentication;

import com.seeu.darkside.gateway.admin.AdminAuthenticationException;
import com.seeu.darkside.gateway.authentication.admin.AdminLoginBody;
import com.seeu.darkside.gateway.admin.AdminService;
import com.seeu.darkside.gateway.authentication.app.AuthenticationService;
import com.seeu.darkside.gateway.authentication.app.FacebookRequestException;
import com.seeu.darkside.gateway.authentication.app.LoginBody;
import com.seeu.darkside.gateway.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.tuple.Tuple;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.seeu.darkside.gateway.security.TokenAuthentication.TOKEN_PREFIX;

@RestController
@RequestMapping("/login")
public class LoginController {

	private final AuthenticationService authenticationService;
	private final AdminService adminService;

	@Autowired
	public LoginController(AuthenticationService authenticationService, AdminService adminService) {
		this.authenticationService = authenticationService;
		this.adminService = adminService;
	}

	@PostMapping
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

	@PostMapping("/admin")
	public ResponseEntity loginAdmin(@RequestBody AdminLoginBody adminLoginBody) {
		final HttpHeaders headers = new HttpHeaders();
		HttpStatus status;
		try {
			final String token = adminService.authenticateAdmin(adminLoginBody.getEmail(), adminLoginBody.getPassword());

			headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + token);
			status = HttpStatus.OK;
		} catch (AdminAuthenticationException e) {
			status = HttpStatus.UNAUTHORIZED;
		}

		return new ResponseEntity<>(headers, status);
	}
}
