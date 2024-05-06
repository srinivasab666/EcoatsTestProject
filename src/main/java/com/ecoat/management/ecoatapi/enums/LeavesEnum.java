package com.ecoat.management.ecoatapi.enums;

public enum LeavesEnum {
	BEREAVEMENT(Constants.BEREAVEMENT), EMERGENCY_LEAVE(Constants.EMERGENCY_LEAVE), HOLIDAY(Constants.HOLIDAY),
	SICK(Constants.SICK), UNPAID_LEAVE(Constants.UNPAID_LEAVE), VACATION(Constants.VACATION);

	private String leave;

	private LeavesEnum(String leave) {
		this.leave = leave;
	}

	public String getLeave() {
		return leave;
	}

	public static LeavesEnum find(String leave) {
		for (LeavesEnum leaveType : values()) {
			if (leaveType.getLeave().equalsIgnoreCase(leave)) {
				return leaveType;
			}
		}
		return null;
	}

	public static class Constants {
		public static final String BEREAVEMENT = "Bereavement";
		public static final String EMERGENCY_LEAVE = "Emergency Leave";
		public static final String HOLIDAY = "Holiday";
		public static final String MILITARY_LEAVE = "Military Leave";
		public static final String SICK = "Sick";
		public static final String UNPAID_LEAVE = "Unpaid Leave";
		public static final String VACATION = "Vacation";
	}
    
}
