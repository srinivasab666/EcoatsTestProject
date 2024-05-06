package com.ecoat.management.ecoatapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoat.management.ecoatapi.dto.CorporateTimesheetSettingDTO;
import com.ecoat.management.ecoatapi.dto.DeleteTimesheetDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetApproveDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetSettingDTO;
import com.ecoat.management.ecoatapi.dto.UpdateTimeSheetDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.CorporateTimesheetSettings;
import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import com.ecoat.management.ecoatapi.model.TimesheetEntryApproval;
import com.ecoat.management.ecoatapi.model.TimesheetEntryDetail;
import com.ecoat.management.ecoatapi.model.response.TimeSheetDetailsListResponse;
import com.ecoat.management.ecoatapi.service.TimesheetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/timesheet")
@Slf4j
public class TimesheetController {

	private final TimesheetService timesheetService;

	@PostMapping("/submit")
	public ResponseEntity<List<TimesheetEntryDetail>> submitTimesheet(@Valid @RequestBody TimesheetDTO timesheetDTO) {
		String method = "TimesheetController.submitTimesheet ";
		log.info(method + "Enter");
		ResponseEntity<List<TimesheetEntryDetail>> response = null;
		try {
			List<TimesheetEntryDetail> entryDtlsList = timesheetService.submitTimesheet(timesheetDTO);
			response = new ResponseEntity<>(entryDtlsList, HttpStatus.OK);
			log.info(method + "Exit");
		} catch (Exception e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PutMapping("/modify")
	public ResponseEntity<List<TimesheetEntryDetail>> updateTimesheet(
			@Valid @RequestBody UpdateTimeSheetDTO timesheetDTO) {
		String method = "TimesheetController.updateTimesheet ";
		log.info(method + "Enter");
		ResponseEntity<List<TimesheetEntryDetail>> response = null;
		try {
			List<TimesheetEntryDetail> entryDtlsList = timesheetService.updateTimesheet(timesheetDTO);
			response = new ResponseEntity<>(entryDtlsList, HttpStatus.OK);
			log.info(method + "Exit");
		} catch (Exception e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@GetMapping("/employee/{empId}")
	public ResponseEntity<List<TimeSheetDetailsListResponse>> getAllEmployeeTimeSheets(@PathVariable("empId") long empId){
		String method = "TimesheetController.getAllEmployeeTimeSheets ";
		log.info(method + "Enter");
		ResponseEntity<List<TimeSheetDetailsListResponse>> response = null;
		if(empId <= 0) {
			throw new EcoatsException("empID should not be lessthen zero");
		}
		try {
			List<TimeSheetDetailsListResponse> entryDtlsListResp = timesheetService.getAllEmployeeTimesheets(empId);
			response = new ResponseEntity<>(entryDtlsListResp,HttpStatus.OK);
			log.info(method + "Exit");
		} catch (Exception e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			throw new EcoatsException(e.getMessage());
		}
		return response;
	}

	@DeleteMapping("/deleteTimeSheet")
	public ResponseEntity<String> deleteTimeSheet(@RequestBody DeleteTimesheetDTO deleteTSDto) {
		String method = "TimesheetController.deleteTimeSheet ";
        log.info(method + "Enter");
		if (deleteTSDto == null || deleteTSDto.getTimeSheetEntryDtlsIds().isEmpty()) {
			throw new EcoatsException("Timesheet entry details should not be empty");
		}
		try {
			timesheetService.deleteTimeSheetDtls(deleteTSDto.getTimeSheetEntryDtlsIds());
			log.info(method + "Exit");
			return new ResponseEntity<>("the timesheet details deleted successfully",HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
    @GetMapping("/approver/{empId}")
    public ResponseEntity<List<TimesheetEntryDetail>> getAllTimeSheetsByApproverId(@PathVariable("empId") long empId){
        String method = "TimesheetController.getAllTimeSheetsByApproverId ";
        log.info(method + "Enter");
        ResponseEntity<List<TimesheetEntryDetail>> response = null;
        if(empId <= 0) {
            throw new EcoatsException("empID should not be less than zero");
        }
        try {
            List<TimesheetEntryDetail> entryDtlsList = timesheetService.getAllTimesheetsByApproverId(empId);
            response = new ResponseEntity<>(entryDtlsList,HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return response;
    }
	
	@PutMapping("/approve")
	public ResponseEntity<List<TimesheetEntryApproval>> approveTimeSheet(@Valid @RequestBody TimesheetApproveDTO taDto) {
		String method = "TimesheetController.approveTimeSheet ";
		log.info(method +"Enter");
		 ResponseEntity<List<TimesheetEntryApproval>> response = null;
		 
//		if (!ValidationUtil.isValidBooleanType(taDto.getIsApproved())) {
//			throw new EcoatsException("isApproved should be boolean format");
//		}
		 try {
			 List<TimesheetEntryApproval>  tea = timesheetService.approveTimeSheet(taDto);
			 response = new ResponseEntity<>(tea,HttpStatus.OK);
		 }catch (Exception e) {
	            log.error(e.getMessage());
	            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	        }
		log.info(method +"Exit");
		return response;
	}
	
    @PostMapping("/employeeTimesheetSettings")
    public ResponseEntity<EmployeeTimesheetSettings> addEmployeeTSSettings(@Valid @RequestBody TimesheetSettingDTO dto) {
        String method = "TimesheetController.addEmployeeTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<EmployeeTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.addEmployeeTSSettings(dto), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    /*@PutMapping("/employeeTimesheetSettings")
    public ResponseEntity<EmployeeTimesheetSettings> updateEmployeeTSSettings(@Valid @RequestBody TimesheetSettingDTO dto) {
        String method = "TimesheetController.addEmployeeTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<EmployeeTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.updateEmployeeTSSettings(dto), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }*/

    @GetMapping("/employeeTimesheetSettings/{id}")
    public ResponseEntity<EmployeeTimesheetSettings> getEmployeeTSSettingsByEmployeeId(@PathVariable("id") long id) {
        String method = "TimesheetController.getEmployeeTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<EmployeeTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.getEmployeeTSSettings(id), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    @DeleteMapping("/employeeTimesheetSettings/{id}")
    public ResponseEntity<EmployeeTimesheetSettings> deleteEmployeeTSSettings(@PathVariable("id") long id) {
        String method = "TimesheetController.addEmployeeTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<EmployeeTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.deleteEmployeeTSSettings(id), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

//    @PostMapping("/employeeTimesheetApprovalSettings")
//    public ResponseEntity<EmployeeTimesheetApprovalSettings> addEmployeeTSApprovalSettings(@Valid @RequestBody TimesheetApprovalSettingDTO dto) {
//        String method = "TimesheetController.addEmployeeTSSettings ";
//        log.info(method +"Enter");
//        ResponseEntity<EmployeeTimesheetApprovalSettings> response = null;
//        try {
//            response = new ResponseEntity<>(timesheetService.addEmployeeTSApprovalSettings(dto), HttpStatus.OK);
//            log.info(method +"Exit");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//        return response;
//    }
//
//    
//    @PutMapping("/employeeTimesheetApprovalSettings")
//    public ResponseEntity<EmployeeTimesheetApprovalSettings> updateEmployeeTSApprovalSettings(@Valid @RequestBody TimesheetApprovalSettingDTO dto) {
//        String method = "TimesheetController.addEmployeeTSSettings ";
//        log.info(method +"Enter");
//        ResponseEntity<EmployeeTimesheetApprovalSettings> response = null;
//        try {
//            response = new ResponseEntity<>(timesheetService.updateEmployeeTSApprovalSettings(dto), HttpStatus.OK);
//            log.info(method +"Exit");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//        return response;
//    }
//
//    @GetMapping("/employeeTimesheetApprovalSettings/{id}")
//    public ResponseEntity<EmployeeTimesheetApprovalSettings> getEmployeeTSApprovalSettings(@PathVariable("id") long id) {
//        String method = "TimesheetController.addEmployeeTSSettings ";
//        log.info(method +"Enter");
//        ResponseEntity<EmployeeTimesheetApprovalSettings> response = null;
//        try {
//            response = new ResponseEntity<>(timesheetService.getEmployeeTSApprovalSettings(id), HttpStatus.OK);
//            log.info(method +"Exit");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//        return response;
//    }
//
//    @DeleteMapping("/employeeTimesheetApprovalSettings/{id}")
//    public ResponseEntity<EmployeeTimesheetApprovalSettings> deleteEmployeeTSApprovalSettings(@PathVariable("id") long id) {
//        String method = "TimesheetController.addEmployeeTSSettings ";
//        log.info(method +"Enter");
//        ResponseEntity<EmployeeTimesheetApprovalSettings> response = null;
//        try {
//            response = new ResponseEntity<>(timesheetService.deleteEmployeeTSApprovalSettings(id), HttpStatus.OK);
//            log.info(method +"Exit");
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//        return response;
//    }


    @PostMapping("/corporateTimesheetSettings")
    public ResponseEntity<CorporateTimesheetSettings> addCorporateTSSettings(@Valid @RequestBody CorporateTimesheetSettingDTO dto) {
        String method = "TimesheetController.addCorporateTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<CorporateTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.addCorporateTSSettings(dto), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    @PutMapping("/corporateTimesheetSettings")
    public ResponseEntity<CorporateTimesheetSettings> updateCorporateTSSettings(@Valid @RequestBody CorporateTimesheetSettingDTO dto) {
        String method = "TimesheetController.updateCorporateTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<CorporateTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.updateCorporateTSSettings(dto), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    @GetMapping("/corporateTimesheetSettings/{id}")
    public ResponseEntity<CorporateTimesheetSettings> getCorporateTSSettingsByCorporateId(@PathVariable("id") long id) {
        String method = "TimesheetController.getCorporateTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<CorporateTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.getCorporateTSSettings(id), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }

    @DeleteMapping("/corporateTimesheetSettings/{id}")
    public ResponseEntity<CorporateTimesheetSettings> deleteCorporateTSSettings(@PathVariable("id") long id) {
        String method = "TimesheetController.deleteCorporateTSSettings ";
        log.info(method +"Enter");
        ResponseEntity<CorporateTimesheetSettings> response = null;
        try {
            response = new ResponseEntity<>(timesheetService.deleteCorporateTSSettings(id), HttpStatus.OK);
            log.info(method +"Exit");
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return response;
    }
}
