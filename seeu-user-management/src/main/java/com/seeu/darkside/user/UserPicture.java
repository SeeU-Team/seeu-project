package com.seeu.darkside.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPicture {

	private Long id;
	private String pictureKey;
	private String pictureUrl;
}
