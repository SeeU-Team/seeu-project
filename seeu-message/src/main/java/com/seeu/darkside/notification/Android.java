package com.seeu.darkside.notification;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Android {

	private String priority;

	public static Android getAndroidHighPriority() {
		return Android.builder()
				.priority("high")
				.build();
	}
}
