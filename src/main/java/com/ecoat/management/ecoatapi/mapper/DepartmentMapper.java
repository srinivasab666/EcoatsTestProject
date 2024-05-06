package com.ecoat.management.ecoatapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ecoat.management.ecoatapi.dto.DepartmentDTO;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Department;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface DepartmentMapper {

    @Mapping(target = "deptName", source = "deptDto.deptName")
	@Mapping(target = "deptLocation", source = "deptDto.deptLocation")
	@Mapping(target = "isActive", constant = "1")
	@Mapping(target = "corporate", source = "corporate")
	public abstract Department mapDeptDtoToDepartment(Corporate corporate,DepartmentDTO deptDto);
	
}
