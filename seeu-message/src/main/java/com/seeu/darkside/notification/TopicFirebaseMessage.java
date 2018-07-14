package com.seeu.darkside.notification;

import lombok.*;

import java.util.Map;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class TopicFirebaseMessage extends FirebaseMessage {

	private String topic;

	public TopicFirebaseMessage(Map<String, String> data, Android android, String topic) {
		super(data, android);
		this.topic = topic;
	}
}
