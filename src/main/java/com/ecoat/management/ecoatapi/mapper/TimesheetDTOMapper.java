package com.ecoat.management.ecoatapi.mapper;

import java.sql.Timestamp;
import java.util.Date;

import com.ecoat.management.ecoatapi.dto.TimesheetDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetDTO.TsEntryDtls;
import com.ecoat.management.ecoatapi.dto.UpdateTimeSheetDTO;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import com.ecoat.management.ecoatapi.model.Project;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.TimesheetEntryDetail;
import com.ecoat.management.ecoatapi.model.TimesheetEntryStatusCode;

public class TimesheetDTOMapper {
	/*public static EmployeeTimesheetSettings populateEmployeeTimesheetSettings(TimesheetDTO timesheetDTO,
			Employee employee) {
		EmployeeTimesheetSettings employeeTimesheetSettings = new EmployeeTimesheetSettings();
		employeeTimesheetSettings.setEmployee(employee);
		employeeTimesheetSettings.setTimesheetType(timesheetDTO.getTimesheetType());
		employeeTimesheetSettings.setCreatedBy(timesheetDTO.getCreatedBy());
		employeeTimesheetSettings.setCreatedOn(new Date());
		return employeeTimesheetSettings;
	}*/

	public static TimesheetEntry populateTimesheetEntry(TimesheetDTO timesheetDTO,
			EmployeeTimesheetSettings employeeTimesheetSettings, TimesheetEntryStatusCode timesheetEntryStatusCode) {
		TimesheetEntry timesheetEntry = TimesheetEntry.builder().timesheetSettings(employeeTimesheetSettings)
				.fromDate(timesheetDTO.getFromDate()).toDate(timesheetDTO.getToDate())
				.statusCode(timesheetEntryStatusCode).createdBy(employeeTimesheetSettings.getEmployee().getEmail()).createdOn(new Date())
				.isActive(1).appraisalComments(timesheetDTO.getAppraisalStatusComments()).isAppraisalInclude(timesheetDTO.isAppraisalInclude()).build();

		if (timesheetDTO.isSubmitted()) {
			timesheetEntry.setSubmittedDate(new Date());
		} else {
			timesheetEntry.setSubmittedDate(null);
		}
		return timesheetEntry;
	}

	public static TimesheetEntryDetail populateTimesheetEntryDetail(TimesheetDTO timesheetDTO,
			TimesheetEntry timesheetEntry, Project project, Client client, TsEntryDtls entrydtls) {
		TimesheetEntryDetail timesheetEntryDetail = TimesheetEntryDetail.builder().timesheetEntry(timesheetEntry)
				.project(project).client(client).entryDate(entrydtls.getEntryDate()).time(entrydtls.getEntryHours())
				.additionalComments(timesheetDTO.getAdditionalComments()).isActive(1).isBillable(entrydtls.getIsBillable())
				.commentsTag(timesheetDTO.getCommentsTag()).createdBy(timesheetEntry.getTimesheetSettings().getEmployee().getEmail()).createdOn(new Date())
				.serviceName(entrydtls.getServiceName()).build();
		return timesheetEntryDetail;
	}

}
