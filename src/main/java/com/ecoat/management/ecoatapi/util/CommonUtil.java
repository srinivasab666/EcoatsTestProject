package com.ecoat.management.ecoatapi.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.ecoat.management.ecoatapi.model.Department;

public class CommonUtil {

	private static final String YYYYMMDD = "yyyy-MM-dd";
	private static final String YYYYMMDDHHmmss = "yyyy/MM/dd HH:mm:ss";
	private static final String YYYYMMDD235959 = "yyyy/MM/dd 23:59:59";
	
	public static String alphaNumericString(int len) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	public static String generateCode(String name) {
		Random r = new Random(System.currentTimeMillis());
		Integer num = 10000 + r.nextInt(99999);
		return name.substring(0, 2).toUpperCase(Locale.ROOT) + num.toString();
	}

	public static boolean isDuplicateDepartment(List<Department> departments, String deptName) {
		return departments.stream().anyMatch(department -> department.getDeptName().equalsIgnoreCase(deptName));
	}

	public static boolean convertStringToBoolean(String val) {
		return Boolean.parseBoolean(val);
	}

	public static Date convertStringToDateMinMax(String dateString,boolean isEndOftheDay) {
		Date convertedDate = null;
		if (!isEndOftheDay) {
			LocalDate localDate = LocalDate.parse(dateString);
			LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MIN);
			convertedDate = new Date(Timestamp.valueOf(startOfDay).getTime());
		}else {
			LocalDate localDate = LocalDate.parse(dateString);
			LocalDateTime startOfDay = LocalDateTime.of(localDate, LocalTime.MAX);
			 convertedDate = new Date(Timestamp.valueOf(startOfDay).getTime());
		}
		return convertedDate;
	}
	
	public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
	
	public static LocalDate convertToLocalDate(Date dateToConvert) {
		return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
}
