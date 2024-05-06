package com.ecoat.management.ecoatapi.service;

import java.util.List;

import com.ecoat.management.ecoatapi.dto.DepartmentDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.model.Department;

public interface DepartmentService {
public Department addDepartment(DepartmentDTO deptDto) throws CorporateNotFoundException, DepartmentAlreadyExistsException;
public Department getDepartment(Long deptId) throws DepartmentNotFoundException;
public List<Department> getDepartments();
Department editDepartment(DepartmentDTO deptDto) throws CorporateNotFoundException, DepartmentNotFoundException;
public void deleteDepartment(Long deptId, String userId) throws DepartmentNotFoundException;
public List<Department> getDepartementsByCorp(Long corpId) throws DepartmentNotFoundException;
}
