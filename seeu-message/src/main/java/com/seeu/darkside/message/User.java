package com.seeu.darkside.message;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Long id;
	private String name;
	private Long facebookId;
}
