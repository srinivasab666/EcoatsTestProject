package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimeSheetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4772928694745720231L;

	@Min(value = 1, message = "empId can't be less than 1")
	private Long empId;

//	private String timesheetType;

	@Min(value = 1, message = "timesheetEntryId can't be less than 1")
	private Long timesheetEntryId;

	private boolean isSubmitted;

//	private String updatedBy;

	private List<EntryDtls> entryDtlsList;

	private String appraisalStatusComments;

	private boolean isAppraisalInclude;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EntryDtls {
		private Date entryDate;
		private float entryHours;
		private Boolean isBillable;
		private Long timesheetEntryDtsId;
		private String serviceName;
		@Min(value = 1, message = "clientId can't be less than 1")
		private long clientId;
		
		private long projectId;
		private String timeoff;
	}
}
