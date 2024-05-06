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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecoat.management.ecoatapi.dto.ProjectDTO;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.model.Project;
import com.ecoat.management.ecoatapi.service.ProjectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
@Slf4j
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/addProject")
	public ResponseEntity<Project> addClient(@Valid @RequestBody ProjectDTO projectDTO) {
		ResponseEntity<Project> response = null;
		try {
			Project project = projectService.addProject(projectDTO);
			response = new ResponseEntity<>(project, HttpStatus.OK);
		} catch (ClientNotFoundException | CorporateNotFoundException | ProjectNotFoundException e) {
			response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		return response;
	}

	@GetMapping("/getProjectByName")
	public ResponseEntity<Project> getProject(@RequestParam Long projectId) {
		Project project = null;
		ResponseEntity<Project> response = null;
		try {
			project = projectService.getProject(projectId);
			response = new ResponseEntity<>(project, HttpStatus.OK);
		} catch (ProjectNotFoundException e) {
			response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		return response;
	}

	@GetMapping("/getProjects")
	public ResponseEntity<List<Project>> getProjects() {
		List<Project> allProjects = null;
		ResponseEntity<List<Project>> response = null;
		try {
			allProjects = projectService.getProjects();
			response = new ResponseEntity<List<Project>>(allProjects, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}

	@PutMapping("/editProject")
	public ResponseEntity<Project> updateProject(@Valid @RequestBody ProjectDTO projectDTO) {
		Project project = null;
		ResponseEntity<Project> response = null;
		try {
			project = projectService.editProject(projectDTO);
			response = new ResponseEntity<>(project, HttpStatus.OK);
		} catch (ClientNotFoundException | CorporateNotFoundException | ProjectNotFoundException e) {
			response = new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
		return response;
	}

	@GetMapping("/getProjects/corpId/{corpId}")
	public ResponseEntity<List<Project>> getProjectsByCorpId(@PathVariable("corpId") Long corpId) {
		List<Project> projects = null;
		try {
			projects = projectService.getProjectsByCorpId(corpId);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteProject")
	public ResponseEntity<String> deleteProject(@RequestParam Long projectId) {
		ResponseEntity<String> response = null;
		try {
			projectService.deleteProject(projectId);
			response = new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
		} catch (ProjectNotFoundException e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return response;
	}
}
