package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.*;
import com.ecoat.management.ecoatapi.exception.*;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.response.TimesheetReportResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@Service
public interface ReportingService {
    Map<String, Float> getEmployeeWeeklyReport(long employeeId) throws InvalidReportingRequestedException;

    Map<String, Float> getEmployeeMonthlyReport(long employeeId) throws InvalidReportingRequestedException;

    List<TimesheetReportResponse> getEmployeeCustomReport(ReportDTO dto);

	ByteArrayInputStream downloadStatusReport(List<TimesheetEntry> entries, AppraisalCommentsDTO appraisalDto);
}
