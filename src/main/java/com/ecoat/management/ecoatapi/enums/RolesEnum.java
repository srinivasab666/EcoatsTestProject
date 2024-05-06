package com.ecoat.management.ecoatapi.enums;

public enum RolesEnum {

	USER(Constants.USER), ADMIN(Constants.ADMIN), SUPERADMIN(Constants.SUPERADMIN);

	private String role;

	private RolesEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public static RolesEnum find(String role) {
		for (RolesEnum roleType : values()) {
			if (roleType.getRole().equalsIgnoreCase(role)) {
				return roleType;
			}
		}
		return null;
	}

	public static class Constants {
		public static final String USER = "USER";
		public static final String ADMIN = "ADMIN";
		public static final String SUPERADMIN = "SUPER ADMIN";
	}
}
