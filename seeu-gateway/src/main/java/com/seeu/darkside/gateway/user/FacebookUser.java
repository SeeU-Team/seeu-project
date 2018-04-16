package com.seeu.darkside.gateway.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUser {

//	@JsonProperty(value = "id")
	private Long id;

	@JsonProperty(value = "name")
	private String name;
}
