package com.seeu.darkside.notification;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ConditionFirebaseMessage extends FirebaseMessage {

	private String condition;

	public ConditionFirebaseMessage(Map<String, String> data, Android android, String condition) {
		super(data, android);
		this.condition = condition;
	}

	public ConditionFirebaseMessage(Map<String, String> data, Android android, List<String> topics) {
		super(data, android);
		if (topics.size() < 1) {
			throw new IllegalArgumentException("There must be at least one topic");
		}

		condition = "'" + topics.get(0) + "' in topics";
		for (int i = 1; i < topics.size(); i++) {
			condition += " || '" + topics.get(i) + "' in topics";
		}
	}
}
