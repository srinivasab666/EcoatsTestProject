package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.EmployeeBillingDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.model.EmployeeBilling;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeBillingService {

    EmployeeBilling addEmployeeBilling(EmployeeBillingDTO employeeBillingDTO);
    EmployeeBilling updateEmployeeBilling(EmployeeBillingDTO employeeBillingDTO);
    List<EmployeeBilling> getAllEmployeeBilling(long empId);
    EmployeeBilling getEmployeeBilling(long empBillingId);
    String deleteEmployeeBilling(long empBillingId, String updatedBy);
}
