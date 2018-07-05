package com.seeu.darkside.message;

/**
 * Type of conversation.
 * Defines what from and dest are (IDs for user, or team, or merged teams).
 */
public enum ConversationType {
	USER_TO_USER((short) 0),
	USER_TO_TEAM((short) 1),
	USER_TO_NIGHTCENTER((short) 2),
	TEAM_TO_BEFORE((short) 3);

	private short value;

	ConversationType(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}
}
