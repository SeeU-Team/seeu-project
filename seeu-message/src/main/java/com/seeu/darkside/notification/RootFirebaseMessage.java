package com.seeu.darkside.notification;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RootFirebaseMessage<T extends FirebaseMessage> {

	private T message;
}
