package com.seeu.darkside.messages;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Message {

	private String sender;
	private String receiver;
	private String content;
	private Date date;
}
