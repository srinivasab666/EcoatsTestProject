package com.ecoat.management.ecoatapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecoat.management.ecoatapi.dto.DepartmentDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.Department;
import com.ecoat.management.ecoatapi.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/department")
@Slf4j
public class DepartmentController {

	private final DepartmentService deptService;

	@PostMapping("/addDept")
	public ResponseEntity<Department> addDepartment(@Valid @RequestBody DepartmentDTO deptDto)
			throws CorporateNotFoundException, DepartmentAlreadyExistsException {
		Department dept = deptService.addDepartment(deptDto);
		ResponseEntity<Department> response = new ResponseEntity<>(dept, HttpStatus.OK);
		return response;

	}
	
	@GetMapping("/getDept")
	public ResponseEntity<Department> getDepartement(@RequestParam Long deptId){
		ResponseEntity<Department> response= null;
		Department dept = null;
		try {
			dept = deptService.getDepartment(deptId);
			response = new ResponseEntity<>(dept, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}

	@GetMapping("/getDept/corpId/{corpId}")
	public ResponseEntity<List<Department>> getDepartementsByCorp(@PathVariable("corpId") Long corpId){
		ResponseEntity<List<Department>> response= null;
		List<Department> dept = null;
		try {
			dept = deptService.getDepartementsByCorp(corpId);
			response = new ResponseEntity<>(dept, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}
	
	@GetMapping("/getDepartements")
	public ResponseEntity<List<Department>> getDepartements(){
		List<Department> allDepartments = null;
		ResponseEntity<List<Department>> response= null;
		try {
			allDepartments = deptService.getDepartments();
			response = new ResponseEntity<List<Department>>(allDepartments, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}
	
	@PutMapping("/editDept")
	public ResponseEntity<Department> updateDepartement(@Valid @RequestBody DepartmentDTO deptDto){
		Department dept = null;
		ResponseEntity<Department> response= null;
		try {
			dept = deptService.editDepartment(deptDto);
			response = new ResponseEntity<>(dept, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}
	
	@DeleteMapping("/deleteDept")
	public ResponseEntity<String> deleteDepartement(@RequestParam Long deptId,@RequestParam String userId) throws DepartmentNotFoundException{
		ResponseEntity<String> response= null;
		deptService.deleteDepartment(deptId,userId);
		response = new ResponseEntity<>("Department deleted successfully", HttpStatus.OK);
		return response;
	}
}
