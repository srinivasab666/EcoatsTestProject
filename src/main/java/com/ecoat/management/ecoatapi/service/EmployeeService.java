package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.exception.EmployeeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {
    Employee getEmployeeById(long empId) throws EmployeeNotFoundException;
    List<Employee> getEmployees() ;
    List<Employee> getEmplyeesByDept(long deptId) throws EmployeeNotFoundException;
    List<Employee> getEmployeesByCorp(long corpId) throws EmployeeNotFoundException;
    Employee getEmployeeByEmailAddress(String emailAddress) throws EmployeeNotFoundException;
    Employee addEmployee(EmployeeDTO empDTO) throws EmployeeAlreadyExistsException, CorporateNotFoundException, DepartmentNotFoundException;
    Employee updateEmployee(EmployeeDTO empDTO) throws EmployeeNotFoundException, CorporateNotFoundException, DepartmentNotFoundException;
    void deleteEmployee(long empId, String emailAddress) throws EmployeeNotFoundException;
	Employee getManagerByEmpId(long employeeId) throws EmployeeNotFoundException;
    List<Employee> getEmployeesBySupervisorId(long corpId);
}
