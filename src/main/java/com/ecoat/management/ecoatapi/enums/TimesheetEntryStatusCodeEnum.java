package com.ecoat.management.ecoatapi.enums;

public enum TimesheetEntryStatusCodeEnum {

	SAVED(Constants.SAVED), SUBMITTED(Constants.SUBMITTED);

	private String statusCode;

	private TimesheetEntryStatusCodeEnum(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public static TimesheetEntryStatusCodeEnum find(String statusCode) {
		for (TimesheetEntryStatusCodeEnum code : values()) {
			if (code.getStatusCode().equalsIgnoreCase(statusCode)) {
				return code;
			}
		}
		return null;
	}

	public static class Constants {
		public static final String SAVED = "SAVED";
		public static final String SUBMITTED = "SUBMITTED";
	}

}
