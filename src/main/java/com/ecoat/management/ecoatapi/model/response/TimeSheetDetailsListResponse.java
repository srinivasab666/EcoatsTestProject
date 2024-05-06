package com.ecoat.management.ecoatapi.model.response;

import com.ecoat.management.ecoatapi.model.TimesheetEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetDetailsListResponse {

	private TimesheetEntry timesheetEntry;
	private String approvalStatus;
	
}
