package com.seeu.darkside.message;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompleteMessageDto<T> {

	protected Long id;
	protected String content;
	protected T owner;

	protected Date created;
	protected Date updated;
}
