package com.ecoat.management.ecoatapi.service;

import java.util.List;

import com.ecoat.management.ecoatapi.dto.*;
import com.ecoat.management.ecoatapi.model.*;
import com.ecoat.management.ecoatapi.model.response.TimeSheetDetailsListResponse;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;

@Service
public interface TimesheetService {
    List<TimesheetEntryDetail> submitTimesheet(TimesheetDTO timesheetDTO) throws ProjectNotFoundException, ClientNotFoundException;

	List<TimesheetEntryDetail> updateTimesheet(UpdateTimeSheetDTO timesheetDTO);

	List<TimeSheetDetailsListResponse> getAllEmployeeTimesheets(Long empId);
    List<TimesheetEntryDetail> getAllTimesheetsByApproverId(Long empId);

	EmployeeTimesheetSettings addEmployeeTSSettings(TimesheetSettingDTO dto) throws EcoatsException;
    EmployeeTimesheetSettings getEmployeeTSSettings(long id) throws EcoatsException;
    EmployeeTimesheetSettings deleteEmployeeTSSettings(long id) throws EcoatsException;

    CorporateTimesheetSettings addCorporateTSSettings(CorporateTimesheetSettingDTO dto) throws EcoatsException;
    CorporateTimesheetSettings updateCorporateTSSettings(CorporateTimesheetSettingDTO dto) throws EcoatsException;
    CorporateTimesheetSettings getCorporateTSSettings(long id) throws EcoatsException;
    CorporateTimesheetSettings deleteCorporateTSSettings(long id) throws EcoatsException;

//    EmployeeTimesheetApprovalSettings addEmployeeTSApprovalSettings(TimesheetApprovalSettingDTO dto) throws EcoatsException;
//    EmployeeTimesheetApprovalSettings updateEmployeeTSApprovalSettings(TimesheetApprovalSettingDTO dto) throws EcoatsException;
//    EmployeeTimesheetApprovalSettings getEmployeeTSApprovalSettings(long id) throws EcoatsException;
//    EmployeeTimesheetApprovalSettings deleteEmployeeTSApprovalSettings(long id) throws EcoatsException;

	List<TimesheetEntryApproval> approveTimeSheet(TimesheetApproveDTO taDto) throws EmployeeNotFoundException;

	void deleteTimeSheetDtls(List<Long> timeSheetEntryDtlsIds);

}
