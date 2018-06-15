package com.seeu.darkside.rs;

import com.seeu.darkside.messages.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class MessagesController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Message newMessage(Message message) {
		message.setDate(new Date());
		return message;
	}
}
