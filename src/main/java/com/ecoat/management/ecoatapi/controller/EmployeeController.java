package com.ecoat.management.ecoatapi.controller;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.EmployeeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.exception.UserNotFoundException;
import com.ecoat.management.ecoatapi.model.Employee;
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
public class EmployeeController {

	private final EmployeeService employeeService;
	private final MailerService mailerService;

	@GetMapping("/employee/id/{employeeId}")
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable("employeeId") long empId) {
		try {
			Employee employee = employeeService.getEmployeeById(empId);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>(new Employee(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/employee/manager/id/{employeeId}")
	public ResponseEntity<Employee> getManagerDetails(@PathVariable("employeeId") long empId) {
		try {
			Employee employee = employeeService.getManagerByEmpId(empId);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>(new Employee(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployeeDetails() {
		List<Employee> employees = employeeService.getEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping("/employees/deptId/{deptId}")
	public ResponseEntity<List<Employee>> getEmployeesByDept(@PathVariable("deptId") long empId) {
		try {
			List<Employee> employee = employeeService.getEmplyeesByDept(empId);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/employees/corpId/{corpId}")
	public ResponseEntity<List<Employee>> getEmployeesByCorp(@PathVariable("corpId") long corpId) {
		try {
			List<Employee> employee = employeeService.getEmployeesByCorp(corpId);
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/employees/supId/{supId}")
	public ResponseEntity<List<Employee>> getEmployeesBySupervisor(@PathVariable("supId") long supId) {
		List<Employee> employee = employeeService.getEmployeesBySupervisorId(supId);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@GetMapping("/employee/email/{emailAddress}")
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable("emailAddress") String emailAddress) {
		try {
			return new ResponseEntity<>(employeeService.getEmployeeByEmailAddress(emailAddress), HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>(new Employee(), HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/employee")
	public ResponseEntity<Employee> addEmployeeDetails(@Valid @RequestBody EmployeeDTO employeeDTO) {
		try {
			Employee emp = employeeService.addEmployee(employeeDTO);
			mailerService.sendForgotPasswordLink(emp.getEmail());
			return new ResponseEntity<>(emp, HttpStatus.CREATED);
		} catch (EmployeeAlreadyExistsException | CorporateNotFoundException | DepartmentNotFoundException
				| UserNotFoundException e) {
			return new ResponseEntity<>(new Employee(), HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/employee")
	public ResponseEntity<Employee> updateEmployeeDetails(@Valid @RequestBody EmployeeDTO employeeDTO) {
		try {
			if(null != employeeDTO && employeeDTO.getEmpId()!=0){
				return new ResponseEntity<>(employeeService.updateEmployee(employeeDTO), HttpStatus.CREATED);
			}else{
				return new ResponseEntity<>(new Employee(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
	}

	@DeleteMapping("/employee/{employeeId}/email/{emailAddress}")
	public ResponseEntity<String> deleteEmployeeDetails(@PathVariable("employeeId") long empId,
			@PathVariable("emailAddress") String emailAddress) {
		try {
			employeeService.deleteEmployee(empId, emailAddress);
			return new ResponseEntity<>("Employee delete successfully", HttpStatus.OK);
		} catch (EmployeeNotFoundException employeeNotFoundException) {
			return new ResponseEntity<>("Specified Employee does not exist", HttpStatus.CONFLICT);
		}
	}
}
