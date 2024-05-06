package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.EmployeeBillingDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeBilling;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import com.ecoat.management.ecoatapi.repository.EmployeeBillingRepository;
import com.ecoat.management.ecoatapi.repository.EmployeeGoalsRepository;
import com.ecoat.management.ecoatapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeBillingServiceImpl implements EmployeeBillingService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeBillingRepository employeeBillingRepository;

    @Override
    public EmployeeBilling addEmployeeBilling(EmployeeBillingDTO employeeBillingDTO) {
        EmployeeBilling eb = employeeBillingRepository.findByEmployeeIdAndBillingType(employeeBillingDTO.getEmployeeId(),
                employeeBillingDTO.getBillingType());
        if(null == eb){
            Optional<Employee> employee = employeeRepo.findById(employeeBillingDTO.getEmployeeId());
            EmployeeBilling employeeBilling = EmployeeBilling.builder().employee(employee.get()).billingRate(employeeBillingDTO.getBillingRate())
                    .billingType(employeeBillingDTO.getBillingType()).isActive(1).createdBy(employeeBillingDTO.getCreatedBy())
                    .createdOn(new Date()).build();
            return employeeBillingRepository.saveAndFlush(employeeBilling);
        }else{
            throw new EcoatsException("Employee billing already present");
        }
    }

    @Override
    public EmployeeBilling updateEmployeeBilling(EmployeeBillingDTO employeeBillingDTO) {
        Optional<EmployeeBilling> eb = employeeBillingRepository.findById(employeeBillingDTO.getEmployeeBillingId());
        if(eb.isPresent()){
            eb.get().setBillingRate(employeeBillingDTO.getBillingRate());
            eb.get().setBillingType(employeeBillingDTO.getBillingType());
            eb.get().setUpdatedBy(employeeBillingDTO.getUpdatedBy());
            eb.get().setUpdatedOn(new Date());
            return employeeBillingRepository.saveAndFlush(eb.get());
        }else{
            throw new EcoatsException("Employee billing not present");
        }
    }

    @Override
    public List<EmployeeBilling> getAllEmployeeBilling(long empId) {
        return employeeBillingRepository.findAllEmployeeBillingDetails(empId);
    }

    @Override
    public EmployeeBilling getEmployeeBilling(long empBillingId) {
        return employeeBillingRepository.findById(empBillingId).get();
    }

    @Override
    public String deleteEmployeeBilling(long empBillingId, String updatedBy) {
        Optional<EmployeeBilling> eb = employeeBillingRepository.findById(empBillingId);
        if(eb.isPresent()){
            eb.get().setIsActive(0);
            eb.get().setUpdatedBy(updatedBy);
            eb.get().setUpdatedOn(new Date());
            employeeBillingRepository.saveAndFlush(eb.get());
            return "Employee billing deleted successfully..";
        }else{
            return "Employee billing not present";
        }
    }
}
