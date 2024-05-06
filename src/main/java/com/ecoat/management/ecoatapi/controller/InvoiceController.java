package com.ecoat.management.ecoatapi.controller;

import java.io.IOException;
import java.util.List;

import com.ecoat.management.ecoatapi.dto.ReportDTO;
import com.ecoat.management.ecoatapi.model.response.TimesheetReportResponse;
import com.ecoat.management.ecoatapi.service.ReportingService;
import org.springframework.web.bind.annotation.*;

import com.ecoat.management.ecoatapi.service.InvoiceGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/invoice")
@Slf4j
public class InvoiceController {

    private final InvoiceGenerator invoiceGenerator;
    private final ReportingService reportingService;

    @PostMapping("/generate")
    public void generatePDFInvoice(@RequestBody ReportDTO reportsDTO) throws IOException{
    	String method = "InvoiceController.generatePDFInvoice ";
		log.info(method + "Enter");
        List<TimesheetReportResponse> customReport = reportingService.getEmployeeCustomReport(reportsDTO);
        boolean unApprovedTS = false;
        double totHours = 0.0;
        for(TimesheetReportResponse tsReport: customReport){
            if(!"approved".equalsIgnoreCase(tsReport.getApprovalStatus())){
                unApprovedTS = true;
                break;
            }else{
                totHours = totHours+tsReport.getTotalBilledHours();
            }
        }
        if(!unApprovedTS){
            invoiceGenerator.generateInvoice(totHours, reportsDTO);
        }else{
            log.info("some of the Timesheets are unapproved..");
        }
        log.info(method + "Exit");
    }
    
    
}
