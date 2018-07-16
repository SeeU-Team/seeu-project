package com.seeu.darkside.user;

public enum TeammateStatus {
	LEADER((short) 0),
	MEMBER((short) 1);

	private short value;

	TeammateStatus(short value) {
		this.value = value;
	}

	public short getValue() {
		return value;
	}
}
