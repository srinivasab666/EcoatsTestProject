package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTimesheetDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2100711134417005119L;
	
	private List<Long> timeSheetEntryDtlsIds;
}
