package com.ecoat.management.ecoatapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetReportResponse {
    private String dateRange;
    private Float totalBilledHours;
    private String approvalStatus;
    private String approvalBy;
    private String approvedDate;
    private String reportStatus;
}
