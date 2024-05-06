package com.ecoat.management.ecoatapi.dto;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetApproveDTO {

	@Min(value = 1,message = "entryApproveId can't be less than 1")
	private long timesheetEntryId;
	private boolean isApproved;
	@Min(value = 1,message = "empId can't be less than 1")
	private long approverEmpId;
	private String comments;
	
}
