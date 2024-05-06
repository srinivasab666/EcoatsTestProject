package com.ecoat.management.ecoatapi.mapper;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(target = "empCode", source = "regDto.userId")
    @Mapping(target = "firstName", source = "regDto.userFirstName")
    @Mapping(target = "lastName", source = "regDto.userLastName")
    @Mapping(target = "email", source = "regDto.userEmail")
	@Mapping(target = "createdBy", source = "regDto.userEmail")
	@Mapping(target = "createdOn", expression = "java(new java.util.Date())")
	@Mapping(target = "isActive", constant = "1")
	@Mapping(target = "corporate", source = "corporate")
	public abstract Employee mapRegDtoToEmployee(Corporate corporate,RegistrationDTO regDto);

	@Mapping(target = "empCode", source = "empDTO.employeeCode")
	@Mapping(target = "firstName", source = "empDTO.firstName")
	@Mapping(target = "lastName", source = "empDTO.lastName")
	@Mapping(target = "email", source = "empDTO.emailAddress")
	@Mapping(target = "isActive", constant = "1")
	@Mapping(target = "corporate", source = "corporate")
	public abstract Employee mapEmployeeDtoToEmployee(Corporate corporate, EmployeeDTO empDTO);
}
