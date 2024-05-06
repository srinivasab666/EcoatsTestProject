package com.ecoat.management.ecoatapi.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.ReportDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.response.TimesheetReportResponse;
import com.ecoat.management.ecoatapi.service.AppraisalService;
import com.ecoat.management.ecoatapi.service.ReportingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
@Slf4j
public class ReportingController {

    private final ReportingService reportingService;
    private final AppraisalService appraisalService;

    @GetMapping("/weekly/{empId}")
    public ResponseEntity<Map<String, Float>> getEmployeeWeeklyReport(@PathVariable("empId") Long empId){
        String method = "ReportingController.getEmployeeWeeklyReport";
        log.info(method + "Enter");
        ResponseEntity<Map<String, Float>> response = null;
        if(empId <= 0) {
            throw new EcoatsException("empID should not be less then zero");
        }
        try {
            Map<String, Float> weeklyReport = reportingService.getEmployeeWeeklyReport(empId);
            response = new ResponseEntity<>(weeklyReport, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    @GetMapping("/monthly/{empId}")
    public ResponseEntity<Map<String, Float>> getEmployeeMonthyReport(@PathVariable("empId") Long empId){
        String method = "ReportingController.getEmployeeMonthyReport";
        log.info(method + "Enter");
        ResponseEntity<Map<String, Float>> response = null;
        if(empId <= 0) {
            throw new EcoatsException("empID should not be less then zero");
        }
        try {
            Map<String, Float> monthyReport = reportingService.getEmployeeMonthlyReport(empId);
            response = new ResponseEntity<>(monthyReport, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @PostMapping("/custom")
    public ResponseEntity<List<TimesheetReportResponse>> getEmployeeCustomReport(@RequestBody ReportDTO reportsDTO){
        String method = "ReportingController.getEmployeeMonthyReport";
        log.info(method + "Enter");
        ResponseEntity<List<TimesheetReportResponse>> response = null;
       try {
    	   HttpHeaders headers = new HttpHeaders();
           headers.add("Content-Disposition", "inline; filename=statusreport.pdf");
           List<TimesheetReportResponse> customReport = reportingService.getEmployeeCustomReport(reportsDTO);
            response = new ResponseEntity<>(customReport, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }
    
	@PostMapping(value = "/download/statusreport",produces = 
            MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadStatusReport(
			@Valid @RequestBody AppraisalCommentsDTO appraisalDto) {
		String method = "ReportingController.downloadStatusReport ";
		log.info(method + "Enter");
		log.info("empid : " + appraisalDto.getEmpID() + " From Date: " + appraisalDto.getFromDate() + " To Date: "
				+ appraisalDto.getToDate());
		try {
			List<TimesheetEntry> entries = appraisalService.getAllActiveTimesheetsByDate(appraisalDto);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "inline;filename=statusreport.pdf");
			ByteArrayInputStream out = reportingService.downloadStatusReport(entries, appraisalDto);
			log.info(method + "Exit");
			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(new InputStreamResource(out));
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
	}
}
