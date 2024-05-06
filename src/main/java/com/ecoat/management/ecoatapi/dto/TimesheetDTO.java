package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -175682196044829504L;

	@Min(value = 1, message = "empId can't be less than 1")
	private Long empId;

//	private String timesheetType;

	@NotNull(message = "fromDate cannot be null")
	private Date fromDate;

	@NotNull(message = "toDate cannot be null")
	private Date toDate;

	private boolean isSubmitted;

//	@NotEmpty(message = "statusCode cannot be empty")
	private String additionalComments;

//	@NotEmpty(message = "statusCode cannot be empty")
	private String commentsTag;

//	private String createdBy;
//
//	private String updatedBy;

	private List<TsEntryDtls> entryDtlsList;
	
	private String appraisalStatusComments;

	private boolean isAppraisalInclude;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TsEntryDtls {
		private Date entryDate;
		private float entryHours;
		private Boolean isBillable;

		@NotNull(message = "serviceName cannot be null")
		private String serviceName;
		
		@Min(value = 1, message = "clientId can't be less than 1")
		private long clientId;

		private long projectId;
		
		private String timeoff;
	}
}
