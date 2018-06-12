package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.FacebookUser;
import com.seeu.darkside.gateway.user.User;
import com.seeu.darkside.gateway.user.UserServiceProxy;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String FACEBOOK_USER_INFO_URL = "https://graph.facebook.com/me?fields=id,name&access_token=";

	@Autowired
	UserServiceProxy userServiceProxy;

	@Override
	public String getAuthenticationToken(@NotNull String accessToken) throws FacebookRequestException {
		FacebookUser facebookUser;
		User user = null;

		try {
			final RestTemplate restTemplate = new RestTemplate();
			facebookUser = restTemplate.getForObject(FACEBOOK_USER_INFO_URL + accessToken, FacebookUser.class);
		} catch (RestClientException e) {
			throw new FacebookRequestException("An error occurred while trying to get user information", e);
		}

		if (null == facebookUser) {
			throw new FacebookRequestException("The user information could not been retrieve from Facebook");
		}

		try {
			user = userServiceProxy.getOneByFacebookId(facebookUser.getId());
		} catch (FeignException e) {
			if (e.status() == HttpStatus.NOT_FOUND.value()) {
				// TODO: if user doesn't already exist in DB, add it without any check ?? Or get a signal from caller that this is a first connection ??
				// throw new UsernameNotFoundException("The user doesn't exist yet in the database");
				user = createNewUser(facebookUser);
			} else {
				throw e;
			}
		}

		return TokenAuthenticationUtil.generateToken(user);
	}

	private User createNewUser(FacebookUser facebookUser) {
		final User user = User.builder()
				.facebookId(facebookUser.getId())
				.firstname(facebookUser.getName())
				.lastname("")
				.email("toto@toto.fr")
				.password("Azerty1234")
				.description("")
				.profilePhotoUrl("")
				.build();

		return userServiceProxy.createNewUser(user);
	}
}
