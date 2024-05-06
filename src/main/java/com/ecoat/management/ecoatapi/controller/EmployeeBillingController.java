package com.ecoat.management.ecoatapi.controller;

import com.ecoat.management.ecoatapi.dto.EmployeeBillingDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.EmployeeBilling;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import com.ecoat.management.ecoatapi.service.EmployeeBillingService;
import com.ecoat.management.ecoatapi.service.EmployeeGoalsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmployeeBillingController {

	private final EmployeeBillingService employeeBillingService;

	@GetMapping("/employeebilling/billingid/{billingId}")
	public ResponseEntity<EmployeeBilling> getEmployeeBilling(@PathVariable("billingId") long billingId) {
		EmployeeBilling eb = employeeBillingService.getEmployeeBilling(billingId);
		return new ResponseEntity<>(eb, HttpStatus.OK);
	}

	@GetMapping("/employeebilling/empid/{empId}")
	public ResponseEntity<List<EmployeeBilling>> getAllEmployeeBillingByEmployeeId(@PathVariable("empId") long empId) {
		List<EmployeeBilling> ebs = employeeBillingService.getAllEmployeeBilling(empId);
		return new ResponseEntity<>(ebs, HttpStatus.OK);
	}

	@PostMapping("/employeebilling")
	public ResponseEntity<EmployeeBilling> addEmployeeBilling(@Valid @RequestBody EmployeeBillingDTO employeeBillingDTO) {
		try{
			return new ResponseEntity<>(employeeBillingService.addEmployeeBilling(employeeBillingDTO), HttpStatus.CREATED);
		}catch (Exception e){
			return new ResponseEntity<>(new EmployeeBilling(), HttpStatus.CONFLICT);
		}

	}

	@PutMapping("/employeebilling")
	public ResponseEntity<EmployeeBilling> updateEmployeeBilling(@Valid @RequestBody EmployeeBillingDTO employeeBillingDTO) {
		try {
			return new ResponseEntity<>(employeeBillingService.updateEmployeeBilling(employeeBillingDTO), HttpStatus.CREATED);
		} catch (EcoatsException exception) {
			return new ResponseEntity<>(new EmployeeBilling(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/employeebilling/{billingId}/email/{emailAddress}")
	public ResponseEntity<String> deleteEmployeeBilling(@PathVariable("billingId") long billingId,
			@PathVariable("emailAddress") String emailAddress) {
		return new ResponseEntity<>(employeeBillingService.deleteEmployeeBilling(billingId, emailAddress), HttpStatus.OK);
	}
}
