package com.seeu.darkside.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUser {

//	@JsonProperty(value = "id")
	private Long id;

//	@JsonProperty(value = "name")
	private String name;

//	@JsonProperty(value = "friends")
	private Friends friends;
}
