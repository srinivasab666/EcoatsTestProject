package com.ecoat.management.ecoatapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecoat.management.ecoatapi.dto.ProjectDTO;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

	@Mapping(target = "projectName", source = "projectDto.projectName")
	@Mapping(target = "projectDescription", source = "projectDto.projectDescription")
	@Mapping(target = "client", source = "client")
	@Mapping(target = "createdBy", source = "projectDto.userId")
	@Mapping(target = "isActive", constant = "1")
	Project mapProjectDtoToProject(ProjectDTO projectDto,Client client);
	
	
	@Mapping(target = "projectId", source = "projectDto.projectId")
	@Mapping(target = "projectName", source = "projectDto.projectName")
	@Mapping(target = "projectDescription", source = "projectDto.projectDescription")
	@Mapping(target = "client", source = "client")
	@Mapping(target = "updatedBy", source = "projectDto.userId")
	@Mapping(target = "updatedOn", expression = "java(new java.util.Date())")
	@Mapping(target = "isActive", constant = "1")
	Project mapUpdatedProjectDtoToProject(ProjectDTO projectDto,Client client);
}
