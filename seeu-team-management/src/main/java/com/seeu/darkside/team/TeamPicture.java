package com.seeu.darkside.team;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamPicture {

	private Long id;
	private String pictureKey;
	private String pictureUrl;
}
