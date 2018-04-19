package com.seeu.darkside.gateway.security;

import com.seeu.darkside.gateway.user.FacebookUser;
import com.seeu.darkside.gateway.user.User;
import com.seeu.darkside.gateway.user.UserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String FACEBOOK_USER_INFO_URL = "https://graph.facebook.com/me?fields=id,name&access_token={token}";

	@Autowired
	UserServiceProxy userServiceProxy;

	@Override
	public String getAuthenticationToken(@NotNull String accessToken) throws FacebookRequestException {
		FacebookUser facebookUser;

		try {
			final RestTemplate restTemplate = new RestTemplate();
			facebookUser = restTemplate.getForObject(FACEBOOK_USER_INFO_URL, FacebookUser.class, accessToken);
		} catch (RestClientException e) {
			throw new FacebookRequestException("An error occurred while trying to get user information", e);
		}

		if (null == facebookUser) {
			throw new FacebookRequestException("The user information could not been retrieve from Facebook");
		}

		User user = userServiceProxy.getOneByFacebookId(facebookUser.getId());

		if (null == user) {
			// TODO: if user doesn't already exist in DB, add it without any check ?? Or get a signal from caller that this is a first connection ??
			throw new UsernameNotFoundException("The user doesn't exist yet in the database");
		}

		return TokenAuthenticationUtil.generateToken(user);
	}
}
