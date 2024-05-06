package com.ecoat.management.ecoatapi.enums;

public enum TimeSheetApprovalStatusCodeEnum {

	APPROVED(Constants.APPROVED), PENDING(Constants.PENDING), REJECTED(Constants.REJECTED);

	private String statusCode;

	private TimeSheetApprovalStatusCodeEnum(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public static TimeSheetApprovalStatusCodeEnum find(String statusCode) {
		for (TimeSheetApprovalStatusCodeEnum code : values()) {
			if (code.getStatusCode().equalsIgnoreCase(statusCode)) {
				return code;
			}
		}
		return null;
	}

	public static class Constants {
		public static final String APPROVED = "APPROVED";
		public static final String PENDING = "PENDING";
		public static final String REJECTED = "REJECTED";
	}

}
