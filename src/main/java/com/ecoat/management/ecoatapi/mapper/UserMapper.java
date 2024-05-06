package com.ecoat.management.ecoatapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "userId", source = "regDto.userEmail")
	@Mapping(target = "retries", constant = "0")
	@Mapping(target = "locked", constant = "0")
	@Mapping(target = "createdBy", source = "regDto.userEmail")
	@Mapping(target = "updatedBy", source = "regDto.userEmail")
	@Mapping(target = "isActive", constant = "0")
	@Mapping(target = "corporate", source = "corporate")
	@Mapping(target = "employee", source = "employee")
	@Mapping(target = "createdOn", expression = "java(new java.util.Date())")
	@Mapping(target = "updatedOn", expression = "java(new java.util.Date())")
	public abstract User mapRegDtoToUser(Corporate corporate, Employee employee, RegistrationDTO regDto);
}
