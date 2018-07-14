package com.seeu.darkside.notification;

import lombok.*;

import java.util.Map;

@Getter
@Setter
public abstract class FirebaseMessage {

	private Map<String, String> data;
	private Android android;

	protected FirebaseMessage(Map<String, String> data, Android android) {
		this.data = data;
		this.android = android;
	}

	protected FirebaseMessage() {
	}
}
