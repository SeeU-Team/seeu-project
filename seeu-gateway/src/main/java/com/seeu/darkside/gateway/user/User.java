package com.seeu.darkside.gateway.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

	private Long idUser;

	private Long facebookId;

	private String name;
}
