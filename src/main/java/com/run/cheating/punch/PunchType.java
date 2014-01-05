package com.run.cheating.punch;

public enum PunchType {

	PUNCH_IN_AM(Constants.TYPE_AM_IN), PUNCH_OUT_AM(Constants.TYPE_AM_OUT), PUNCH_IN_PM(
			Constants.TYPE_PM_IN), PUNCH_OUT_PM(Constants.TYPE_PM_OUT);

	private final String type;

	private PunchType(String type) {
		this.type = type;
	}
	
	public String getValue() {
	     return type;
	}
}
