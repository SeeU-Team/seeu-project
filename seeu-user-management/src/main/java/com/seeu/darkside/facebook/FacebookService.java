package com.seeu.darkside.facebook;

import java.util.List;

public interface FacebookService {

	List<FacebookUser> getFacebookUserFriends(String accessToken) throws FacebookRequestException;
}
