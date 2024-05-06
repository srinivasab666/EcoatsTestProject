package com.ecoat.management.ecoatapi.service;

import java.util.List;

import com.ecoat.management.ecoatapi.dto.ProjectDTO;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.model.Project;

public interface ProjectService {
	
	public Project addProject(ProjectDTO projectDto) throws ClientNotFoundException, CorporateNotFoundException, ProjectNotFoundException;
	public Project getProject(Long projectId) throws ProjectNotFoundException;
	public void deleteProject(Long projectId) throws ProjectNotFoundException;
	public List<Project> getProjects();
	Project editProject(ProjectDTO projectDto) throws ClientNotFoundException, CorporateNotFoundException, ProjectNotFoundException;
	List<Project> getProjectsByCorpId(Long corpId);

}
