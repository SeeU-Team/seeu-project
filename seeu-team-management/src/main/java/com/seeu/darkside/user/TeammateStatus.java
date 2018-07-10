package com.seeu.darkside.user;

public enum TeammateStatus {
	LEADER(0),
	MEMBER(1);

	private int value;

	TeammateStatus(int value) {
		this.value = value;
	}
}
