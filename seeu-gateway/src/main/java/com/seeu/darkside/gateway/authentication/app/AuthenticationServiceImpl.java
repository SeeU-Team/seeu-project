package com.seeu.darkside.gateway.authentication.app;

import com.seeu.darkside.gateway.security.TokenAuthentication;
import com.seeu.darkside.gateway.user.FacebookUser;
import com.seeu.darkside.gateway.user.Gender;
import com.seeu.darkside.gateway.user.User;
import com.seeu.darkside.gateway.user.UserServiceProxy;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.tuple.Tuple;
import org.springframework.tuple.TupleBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String FACEBOOK_USER_INFO_URL = "https://graph.facebook.com/me?fields=id,name,gender,picture.type(large)&access_token=";

	private final UserServiceProxy userServiceProxy;
	private final TokenAuthentication tokenAuthentication;

	@Autowired
	public AuthenticationServiceImpl(UserServiceProxy userServiceProxy, TokenAuthentication tokenAuthentication) {
		this.userServiceProxy = userServiceProxy;
		this.tokenAuthentication = tokenAuthentication;
	}

	@Override
	public Tuple getAuthenticationToken(@NotNull LoginBody loginBody) throws FacebookRequestException {
		FacebookUser facebookUser;
		User user;
		boolean isNewUser = false;

		try {
			final RestTemplate restTemplate = new RestTemplate();
			facebookUser = restTemplate.getForObject(FACEBOOK_USER_INFO_URL + loginBody.getAccessToken(), FacebookUser.class);
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
				user = createNewUser(facebookUser, loginBody.getAppInstanceId());
				isNewUser = true;
			} else {
				throw e;
			}
		}

		if (!isNewUser) {
			try {
				userServiceProxy.updateAppInstanceId(user.getId(), loginBody.getAppInstanceId());
			} catch (FeignException e) {
				// Do nothing except log
				Logger.getLogger(this.getClass().getName()).warning("Unable to update instance id of user");
				e.printStackTrace();
			}
		}

		final String token = tokenAuthentication.generateToken(user);

		return TupleBuilder
				.tuple()
				.of("USER", user, "TOKEN", token);
	}

	private User createNewUser(FacebookUser facebookUser, @Nullable String appInstanceId) {
		final User user = User.builder()
				.facebookId(facebookUser.getId())
				.appInstanceId(appInstanceId)
				.name(facebookUser.getName())
				.gender(Gender.valueOf(facebookUser.getGender().toUpperCase()))
				.description("")
				.profilePhotoUrl(facebookUser.getPicture().getData().getUrl())
				.build();

		return userServiceProxy.createNewUser(user);
	}
}
