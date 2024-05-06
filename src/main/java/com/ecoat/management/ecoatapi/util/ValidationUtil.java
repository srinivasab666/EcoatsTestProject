package com.ecoat.management.ecoatapi.util;

import org.springframework.util.StringUtils;

import com.ecoat.management.ecoatapi.exception.EcoatsException;

public class ValidationUtil {

	public static final String MISSING_REQ_PARAMS_MSG = "Missing required parameters";
	
	public static boolean isValidBooleanType(String val) {
		if ("true".equalsIgnoreCase(val) || "false".equalsIgnoreCase(val)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }
	
	public static boolean  isValid(String... vals) {
		boolean isValid = true;
		for (String val : vals) {
			if(!StringUtils.hasText(val)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}
	
	public static void validate(String s) {
		if (!isValid(s)) {
			throw new EcoatsException(ValidationUtil.MISSING_REQ_PARAMS_MSG);
		}
	}
}
