package com.ecoat.management.ecoatapi.controller;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.exception.*;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import com.ecoat.management.ecoatapi.service.EmployeeGoalsService;
import com.ecoat.management.ecoatapi.service.EmployeeService;
import com.ecoat.management.ecoatapi.service.MailerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmployeeGoalsController {

	private final EmployeeGoalsService employeeGoalsService;

	@GetMapping("/employeegoal/{goalId}")
	public ResponseEntity<EmployeeGoals> getEmployeeGoal(@PathVariable("goalId") long goalId) {
		EmployeeGoals employeeGoals = employeeGoalsService.getEmployeeGoal(goalId);
		return new ResponseEntity<>(employeeGoals, HttpStatus.OK);
	}

	@GetMapping("/employeegoals/{empId}")
	public ResponseEntity<List<EmployeeGoals>> getAllGoalsByEmployeeId(@PathVariable("empId") long empId) {
		List<EmployeeGoals> employeeGoals = employeeGoalsService.getAllEmployeeGoals(empId);
		return new ResponseEntity<>(employeeGoals, HttpStatus.OK);
	}

	@PostMapping("/employeegoal")
	public ResponseEntity<EmployeeGoals> addEmployeeGoal(@Valid @RequestBody EmployeeGoalDTO employeeGoalDTO) {
		try{
			return new ResponseEntity<>(employeeGoalsService.addEmployeeGoal(employeeGoalDTO), HttpStatus.CREATED);
		}catch (Exception e){
			return new ResponseEntity<>(new EmployeeGoals(), HttpStatus.CONFLICT);
		}

	}

	@PutMapping("/employeegoal")
	public ResponseEntity<EmployeeGoals> updateEmployeeGoal(@Valid @RequestBody EmployeeGoalDTO employeeGoalDTO) {
		try {
			return new ResponseEntity<>(employeeGoalsService.updateEmployeeGoal(employeeGoalDTO), HttpStatus.CREATED);
		} catch (EcoatsException exception) {
			return new ResponseEntity<>(new EmployeeGoals(), HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/employeegoal/{goalId}/email/{emailAddress}")
	public ResponseEntity<String> deleteEmployeeDetails(@PathVariable("goalId") long goalId,
			@PathVariable("emailAddress") String emailAddress) {
		return new ResponseEntity<>(employeeGoalsService.deleteEmployeeGoal(goalId, emailAddress), HttpStatus.OK);
	}
}
