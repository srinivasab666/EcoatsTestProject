package com.ecoat.management.ecoatapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.ProjectDTO;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.mapper.ProjectMapper;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Project;
import com.ecoat.management.ecoatapi.repository.ClientRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService{

	private final ProjectRepository projectRepo;
	private final ProjectMapper projectMapper;
	private final ClientRepository clientRepo;
	private final CorporateRepository corpRepo;
	
	@Override
	public Project addProject(ProjectDTO projectDto) throws ClientNotFoundException, CorporateNotFoundException, ProjectNotFoundException {
		Project savedProject = null;
		Optional<Project> existingProject = projectRepo.findProjectsByCorpIdAndClientId(projectDto.getCorporateId(), projectDto.getClientId(),projectDto.getProjectName());
		/*long projectExists = existingProjects.get().stream()
				.filter(project -> project.getProjectName().equalsIgnoreCase(projectDto.getProjectName())).count();*/
		if (existingProject.isPresent()) {
				if (existingProject.get().getProjectName().equalsIgnoreCase(projectDto.getProjectName())) {
					if (1 == existingProject.get().getIsActive()) {
						throw new ProjectNotFoundException();
					} else {
						existingProject.get().setIsActive(1);
						existingProject.get().setUpdatedBy(projectDto.getUserId());
						savedProject = projectRepo.save(existingProject.get());
					}
				} else {
					Client client = clientRepo.findById(projectDto.getClientId()).orElseThrow(() -> new ClientNotFoundException());
					Corporate corporate = corpRepo.findById(projectDto.getCorporateId()).orElseThrow(() -> new CorporateNotFoundException());
					Project proj = projectMapper.mapProjectDtoToProject(projectDto, client);
					proj.setCorporate(corporate);
					proj.setCreatedOn(new Date());
					savedProject = projectRepo.saveAndFlush(proj);
				}
		} else {
			Client client = clientRepo.findById(projectDto.getClientId()).orElseThrow(() -> new ClientNotFoundException());
			Corporate corporate = corpRepo.findById(projectDto.getCorporateId()).orElseThrow(() -> new CorporateNotFoundException());
			Project proj = projectMapper.mapProjectDtoToProject(projectDto, client);
			proj.setCorporate(corporate);
			proj.setCreatedOn(new Date());
			savedProject = projectRepo.saveAndFlush(proj);
		}
		return savedProject;
	}

	@Override
	public Project getProject(Long projectId) throws ProjectNotFoundException{
		Project project = null;
		Optional<Project> existingProj = projectRepo.findById(projectId);
		if (existingProj.isPresent()) {
			project = existingProj.get();
		}else {
			throw new ProjectNotFoundException();
		}
		return project;
	}

	@Override
	public void deleteProject(Long projectId) throws ProjectNotFoundException{
		Project project = null;
		Optional<Project> existingProj = projectRepo.findById(projectId);
		if (existingProj.isPresent()) {
			project = existingProj.get();
			project.setIsActive(0);
			projectRepo.saveAndFlush(project);
		}else {
			throw new ProjectNotFoundException();
		}
	}

	@Override
	public List<Project> getProjects() {
		List<Project> projectList = projectRepo.findAll();
		return projectList;
	}

	@Override
	public Project editProject(ProjectDTO projectDto) throws ClientNotFoundException, CorporateNotFoundException, ProjectNotFoundException{
		Project savedProject = null;
		Optional<Project> existingProj = projectRepo.findById(projectDto.getProjectId());
		if (projectDto.getProjectId() != 0 && existingProj.isPresent()) {
			Client client = clientRepo.findById(projectDto.getClientId()).orElseThrow(() -> new ClientNotFoundException());
			Corporate corporate = corpRepo.findById(projectDto.getCorporateId()).orElseThrow(() -> new CorporateNotFoundException());
			Project project = projectMapper.mapUpdatedProjectDtoToProject(projectDto, client);
			project.setCorporate(corporate);
			savedProject = projectRepo.saveAndFlush(project);
		}else {
			throw new ProjectNotFoundException();
		}
		return savedProject;
	}

	@Override
	public List<Project> getProjectsByCorpId(Long corpId){
		List<Project> projectsList = null;
		Optional<List<Project>> projects = projectRepo.findProjectsByCorpId(corpId);
		if (projects.isPresent()) {
			projectsList = projects.get();
		};
		return projectsList;
	}
}
