package com.seeu.darkside.message;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMessage<T> {

	protected Long id;
	protected String content;
	protected T owner;
}
