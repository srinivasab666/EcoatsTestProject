package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.exception.EmployeeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeGoalsService {

    EmployeeGoals addEmployeeGoal(EmployeeGoalDTO employeeGoalDTO);
    EmployeeGoals updateEmployeeGoal(EmployeeGoalDTO employeeGoalDTO);
    List<EmployeeGoals> getAllEmployeeGoals(long empId);
    EmployeeGoals getEmployeeGoal(long goalId);
    String deleteEmployeeGoal(long goalId, String updatedBy);
}
