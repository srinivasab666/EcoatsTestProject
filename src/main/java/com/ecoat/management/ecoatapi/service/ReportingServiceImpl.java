package com.ecoat.management.ecoatapi.service;


import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.ReportDTO;
import com.ecoat.management.ecoatapi.exception.InvalidReportingRequestedException;
import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.TimesheetEntryApproval;
import com.ecoat.management.ecoatapi.model.TimesheetEntryDetail;
import com.ecoat.management.ecoatapi.model.response.TimesheetReportResponse;
import com.ecoat.management.ecoatapi.repository.EmployeeTimesheetSettingsRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetApprovalRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryDetailRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryRepo;
import com.ecoat.management.ecoatapi.util.EcoatsConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

	private final EmployeeTimesheetSettingsRepo empTimesheetStngsRepo;
	private final TimesheetEntryRepo timesheetEntryRepo;
	private final TimesheetEntryDetailRepo timesheetEntryDetailRepo;
	private final TimesheetApprovalRepo timesheetApprovalRepo;
	private final InvoiceGenerator invoiceGenerator;
	

	private final DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
	@Override
	public Map<String, Float> getEmployeeWeeklyReport(long employeeId) throws InvalidReportingRequestedException {
		Map<String,Float> employeeWeeklyReport = new HashMap<>();
		List<TimesheetEntryDetail> timesheetEntryDetails = null;
		Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo
				.findByEmployeeId(employeeId);

		if(employeeTimesheetSettings.isPresent()){
			if(null != employeeTimesheetSettings.get().getCorporateTimesheetSettings() &&
					EcoatsConstants.Timesheet_Type_Weekly
							.equalsIgnoreCase(employeeTimesheetSettings.get().getCorporateTimesheetSettings().getTimesheetType())){
				log.info("Employee timesheet type is Weekly..");
				List<TimesheetEntry> tsEntries = timesheetEntryRepo
						.findByTimesheetSettingId(employeeTimesheetSettings.get().getTimesheetSettingId());
				for(TimesheetEntry tsEntry: tsEntries){
					timesheetEntryDetails = new ArrayList<>();
					timesheetEntryDetails.addAll(timesheetEntryDetailRepo.findByEntryId(tsEntry.getTimesheetEntryId()));
					float totWeeklyHours = 0L;
					for(TimesheetEntryDetail entryDetail : timesheetEntryDetails){
						totWeeklyHours = totWeeklyHours + entryDetail.getTime();
					}
					employeeWeeklyReport.put(formatter1.format(tsEntry.getFromDate()), totWeeklyHours);
				}
			}else if(null != employeeTimesheetSettings.get().getCorporateTimesheetSettings() &&
					EcoatsConstants.Timesheet_Type_Monthly
							.equalsIgnoreCase(employeeTimesheetSettings.get().getCorporateTimesheetSettings().getTimesheetType())){
				log.info("Employee timesheet type is Monthly..");
				throw new InvalidReportingRequestedException();
			}else{
				throw new InvalidReportingRequestedException();
			}
		}
		return employeeWeeklyReport;
	}

	@Override
	public Map<String, Float> getEmployeeMonthlyReport(long employeeId) throws InvalidReportingRequestedException {
		Map<String,Float> employeeMonthlyReport = new HashMap<>();
		List<TimesheetEntryDetail> timesheetEntryDetails = null;
		Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo
				.findByEmployeeId(employeeId);

		if(employeeTimesheetSettings.isPresent()){
			if(null != employeeTimesheetSettings.get().getCorporateTimesheetSettings() &&
					EcoatsConstants.Timesheet_Type_Monthly
							.equalsIgnoreCase(employeeTimesheetSettings.get().getCorporateTimesheetSettings().getTimesheetType())){
				log.info("Employee timesheet type is Monthly..");
				List<TimesheetEntry> tsEntries = timesheetEntryRepo
						.findByTimesheetSettingId(employeeTimesheetSettings.get().getTimesheetSettingId());
				for(TimesheetEntry tsEntry: tsEntries){
					timesheetEntryDetails = new ArrayList<>();
					timesheetEntryDetails.addAll(timesheetEntryDetailRepo.findByEntryId(tsEntry.getTimesheetEntryId()));
					float totWeeklyHours = 0L;
					for(TimesheetEntryDetail entryDetail : timesheetEntryDetails){
						totWeeklyHours = totWeeklyHours + entryDetail.getTime();
					}
					employeeMonthlyReport.put(formatter1.format(tsEntry.getFromDate()), totWeeklyHours);
				}
			}else if(null != employeeTimesheetSettings.get().getCorporateTimesheetSettings() &&
					EcoatsConstants.Timesheet_Type_Weekly
							.equalsIgnoreCase(employeeTimesheetSettings.get().getCorporateTimesheetSettings().getTimesheetType())){
				log.info("Employee timesheet type is Weekly..");
				throw new InvalidReportingRequestedException();
			}else{
				throw new InvalidReportingRequestedException();
			}
		}
		return employeeMonthlyReport;
	}

	@Override
	public List<TimesheetReportResponse> getEmployeeCustomReport(ReportDTO dto) {
		List<TimesheetReportResponse> resp = new ArrayList<>();
		try{
			List<TimesheetEntry> timesheetEntryList = timesheetEntryRepo
					.findTimesheetEntryByFromDateRange(dto.getEmployeeId(),dto.getFromDate(),dto.getToDate());
			Map<Long,TimesheetEntry> entries = new LinkedHashMap<>();
			timesheetEntryList.forEach(entry->{
				entries.put(entry.getTimesheetEntryId(),entry);
			});
			List<TimesheetEntry> entryList = new ArrayList<>();
			for(Map.Entry<Long,TimesheetEntry> entry: entries.entrySet()){
				entryList.add(entry.getValue());
			}
			resp = getcustomDateTimesheetReport(entryList);
		}catch(Exception e){
			log.info("Error occurred in getEmployeeCustomReport"+e.getMessage());
		}
		return resp;
	}

	@Override
	public ByteArrayInputStream downloadStatusReport(List<TimesheetEntry> entries,AppraisalCommentsDTO appraisalDto) {
		String method = "AppraisalServiceImpl.downloadStatusReport ";
		log.info(method + "Enter");
		ByteArrayInputStream out = invoiceGenerator.prepareStatusReportPDF(entries,appraisalDto);
		log.info(method + "Exit");
		return out;
		
		
	}
	private List<TimesheetReportResponse> getcustomDateTimesheetReport(List<TimesheetEntry> timesheetEntryList){
		List<TimesheetReportResponse> resp = new ArrayList<>();
		timesheetEntryList.forEach(entry ->{
			TimesheetReportResponse tResp = new TimesheetReportResponse();
			float totHours = 0L;
			List<TimesheetEntryDetail> timesheetEntryDetailList = timesheetEntryDetailRepo.findByEntryId(entry.getTimesheetEntryId());
			for(TimesheetEntryDetail entryDet : timesheetEntryDetailList){
				totHours = totHours + entryDet.getTime();
			}
			Optional<List<TimesheetEntryApproval>>  optApprovalList = timesheetApprovalRepo.
					getAllActiveTimesheetEntryApprovalByEntryId(entry.getTimesheetEntryId());
			if (optApprovalList.isPresent()) {
				List<TimesheetEntryApproval> approvalList = optApprovalList.get();
				if (approvalList.size() > 0 ) {
					TimesheetEntryApproval ta = approvalList.get(0);
					tResp.setApprovalStatus(ta.getTimesheetApprovalStatusCode().getStatusCode().getStatusCode());
					tResp.setApprovalBy(ta.getApprover().getEmail());
					if(null != ta.getApprovedDate()){
						tResp.setApprovedDate(formatter1.format(ta.getApprovedDate()));
					}
				}
			}
			tResp.setDateRange(formatter1.format(entry.getFromDate())+"-"+formatter1.format(entry.getToDate()));
			tResp.setTotalBilledHours(totHours);
			tResp.setReportStatus("Fetched Successfully");
			resp.add(tResp);
		});
		return resp;
	}
}
