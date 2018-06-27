package com.seeu.darkside.facebook;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacebookServiceImpl implements FacebookService {

	public FacebookServiceImpl() {
	}

	public List<FacebookUser> getFacebookUserFriends(String accessToken) throws FacebookRequestException {
		final RestTemplate restTemplate = new RestTemplate();
		String url = "https://graph.facebook.com/me?fields=id,name,friends.limit(100)&access_token=" + accessToken;
		List<FacebookUser> friends = new ArrayList<>();

		try {
			FacebookUser facebookUser;

			do {
				facebookUser = restTemplate.getForObject(url, FacebookUser.class);
				if (null == facebookUser) {
					throw new FacebookRequestException("An error occurred while trying to get user information");
				}

				friends.addAll(facebookUser.getFriends().getData());

				url = (null != facebookUser.getFriends().getPaging())
						? facebookUser.getFriends().getPaging().getNext() // TODO: check if the url contains the token or we must re-provide it
						: null;
			} while (url != null);
		} catch (RestClientException e) {
			throw new FacebookRequestException("An error occurred while trying to get user information", e);
		}

		return friends;
	}
}
