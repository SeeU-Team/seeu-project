package com.seeu.darkside.message;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMessage {

	private Long id;
	private String content;
	private User owner;

}
