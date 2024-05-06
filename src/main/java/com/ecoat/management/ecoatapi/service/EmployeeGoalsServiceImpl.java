package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.EmployeeGoalDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.*;
import com.ecoat.management.ecoatapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeGoalsServiceImpl implements EmployeeGoalsService {

    private final EmployeeRepository employeeRepo;

    private final EmployeeGoalsRepository employeeGoalsRepository;

    @Override
    public EmployeeGoals addEmployeeGoal(EmployeeGoalDTO employeeGoalDTO) throws EcoatsException{

        Optional<Employee> employee = employeeRepo.findById(employeeGoalDTO.getEmployeeId());
        if(employee.isPresent()){
            EmployeeGoals employeeGoals = EmployeeGoals.builder().employee(employee.get())
                    .goalCategory(employeeGoalDTO.getGoalCategory()).goalDescription(employeeGoalDTO.getGoalDescription())
                    .goalWeightage(employeeGoalDTO.getGoalWeightage()).expiryDate(employeeGoalDTO.getExpiryDate())
                    .createdBy(employeeGoalDTO.getCreatedBy()).createdOn(new Date())
                    .isActive(1).build();
            return employeeGoalsRepository.saveAndFlush(employeeGoals);
        }else{
            throw new EcoatsException("Employee not found");
        }

    }

    @Override
    public EmployeeGoals updateEmployeeGoal(EmployeeGoalDTO employeeGoalDTO) {

        Optional<EmployeeGoals> existingGoal = employeeGoalsRepository.findById(employeeGoalDTO.getEmployeeGoalId());
        if(existingGoal.isPresent()){
            existingGoal.get().setGoalCategory(employeeGoalDTO.getGoalCategory());
            existingGoal.get().setGoalDescription(employeeGoalDTO.getGoalDescription());
            if(null != employeeGoalDTO.getAppraiserRating() &&
                    employeeGoalDTO.getAppraiserRating()>-1){
                existingGoal.get().setAppraiserRating(employeeGoalDTO.getAppraiserRating());
            }
            existingGoal.get().setAppraizeeComments(employeeGoalDTO.getAppraizeeComments());
            existingGoal.get().setGoalWeightage(employeeGoalDTO.getGoalWeightage());
            existingGoal.get().setExpiryDate(employeeGoalDTO.getExpiryDate());
            existingGoal.get().setUpdatedBy(employeeGoalDTO.getUpdatedBy());
            existingGoal.get().setUpdatedOn(new Date());
            return employeeGoalsRepository.save(existingGoal.get());
        }else{
            throw new EcoatsException("Goal not found");
        }
    }

    @Override
    public List<EmployeeGoals> getAllEmployeeGoals(long empId) {
        Optional<List<EmployeeGoals>> employeeGoals = employeeGoalsRepository.findEmployeeGoalsByEmployeeId(empId);
        if(employeeGoals.isPresent()){
            return employeeGoals.get();
        }else {
            return new ArrayList<>();
        }
    }

    @Override
    public EmployeeGoals getEmployeeGoal(long goalId) {
        Optional<EmployeeGoals> employeeGoals = employeeGoalsRepository.findById(goalId);
        if(employeeGoals.isPresent()){
            return employeeGoals.get();
        }else {
            return new EmployeeGoals();
        }
    }

    @Override
    public String deleteEmployeeGoal(long goalId,String updatedBy) {
        Optional<EmployeeGoals> existingGoal = employeeGoalsRepository.findById(goalId);
        if(existingGoal.isPresent()){
            existingGoal.get().setIsActive(0);
            existingGoal.get().setUpdatedBy(updatedBy);
            employeeGoalsRepository.save(existingGoal.get());
            return "EmployeeGoal deleted successfully";
        }else{
            return "EmployeeGoal cannot be found";
        }
    }
}
