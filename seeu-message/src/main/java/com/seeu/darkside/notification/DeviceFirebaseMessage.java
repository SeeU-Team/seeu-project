package com.seeu.darkside.notification;

import lombok.*;

import java.util.Map;

@Getter
@Setter
public class DeviceFirebaseMessage extends FirebaseMessage {

	private String token;

	public DeviceFirebaseMessage(Map<String, String> data, Android android, String token) {
		super(data, android);
		this.token = token;
	}
}
