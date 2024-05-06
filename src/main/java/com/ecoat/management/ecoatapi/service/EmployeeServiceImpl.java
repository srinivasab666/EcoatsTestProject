package com.ecoat.management.ecoatapi.service;

import java.util.*;

import com.ecoat.management.ecoatapi.model.*;
import com.ecoat.management.ecoatapi.repository.*;
import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.enums.RolesEnum;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.EmployeeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.mapper.EmployeeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepo;
    private final CorporateRepository corpRepo;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository deptRepo;
    private final RolesRepository rolesRepo;
    private final UserRepository userRepo;
    private final CorporateTimesheetSettingsRepo corporateTimesheetSettingsRepo;
    private final EmployeeTimesheetSettingsRepo employeeTimesheetSettingsRepo;
    
    @Override
    public Employee getEmployeeById(long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> existingEmployee = employeeRepo.findById(employeeId);
        if(existingEmployee.isPresent()) {
            return existingEmployee.get();
        }else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public Employee getManagerByEmpId(long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> existingEmployee = employeeRepo.findManagerByEmpId(employeeId);
        if(existingEmployee.isPresent()) {
            return existingEmployee.get();
        }else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public List<Employee> getEmployeesBySupervisorId(long supId) {
        return employeeRepo.findAllBySupervisorId(supId);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<Employee> getEmplyeesByDept(long deptId) throws EmployeeNotFoundException {
        Optional<List<Employee>> employees = employeeRepo.findEmployeesByDeptId(deptId);
        if(employees.isPresent()){
            return employees.get();
        }else{
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public List<Employee> getEmployeesByCorp(long corpId) throws EmployeeNotFoundException {
        Optional<List<Employee>> employees = employeeRepo.findEmployeesByCorpId(corpId);
        if(employees.isPresent()){
            return employees.get();
        }else{
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public Employee getEmployeeByEmailAddress(String emailAddress) throws EmployeeNotFoundException {
        Optional<Employee> existingEmployee = employeeRepo.findByEmail(emailAddress);
        if(existingEmployee.isPresent()) {
            return existingEmployee.get();
        }else {
            throw new EmployeeNotFoundException();
        }
    }

	@Override
	public Employee addEmployee(EmployeeDTO empDTO)
			throws EmployeeAlreadyExistsException, CorporateNotFoundException, DepartmentNotFoundException {
        Employee employee = new Employee();
		Optional<Employee> existingEmployee = employeeRepo.findByEmail(empDTO.getEmailAddress());
		Optional<Employee> managerOpt = employeeRepo.findById(empDTO.getSupervisorId());
		Employee manager = managerOpt.isPresent() ? managerOpt.get() : null;
		Set<Role> roleSet = new HashSet<>();
		if (empDTO.getIsPOC()) {
			Optional<Role> existingRole = rolesRepo.findByRoleName(RolesEnum.ADMIN);
			roleSet.add(existingRole.get());
		} else {
			Optional<Role> existingRole = rolesRepo.findByRoleName(RolesEnum.USER);
			roleSet.add(existingRole.get());
		}
		Department dept = deptRepo.findById(empDTO.getDeptId()).orElseThrow(() -> new DepartmentNotFoundException());
		if (existingEmployee.isPresent()) {
			throw new EmployeeAlreadyExistsException();
		} else {
			Optional<Corporate> existingCorporate = corpRepo.findById(empDTO.getCorporateId());
			if (existingCorporate.isPresent()) {
				Employee newEmployee = employeeMapper.mapEmployeeDtoToEmployee(existingCorporate.get(), empDTO);
				newEmployee.setDept(dept);
				newEmployee.setManager(manager);
				newEmployee.setCreatedBy(empDTO.getCreatedByEmail());
				newEmployee.setCreatedOn(new java.util.Date());
				newEmployee.setUpdatedBy(null);
				newEmployee.setUpdatedOn(null);
                if (empDTO.getIsPOC()) {
                    newEmployee.setIs_Poc(1);
                }else{
                    newEmployee.setIs_Poc(0);
                }
				User user = User.builder().userId(empDTO.getEmailAddress()).createdBy(empDTO.getEmailAddress())
						.updatedBy(empDTO.getEmailAddress()).corporate(existingCorporate.get())
						.employee(newEmployee).roles(roleSet).isActive(1).build();
				User savedUser = userRepo.save(user);
				log.info("saving emp TS settings, if corp TS settings available");
                employee =  savedUser.getEmployee();
                saveEmployeeTSSettings(empDTO.getCorporateId(),employee,empDTO.getCreatedByEmail());
			} else {
				throw new CorporateNotFoundException();
			}
		}
		return employee;
	}

    @Override
    public Employee updateEmployee(EmployeeDTO empDTO) throws EmployeeNotFoundException, CorporateNotFoundException, DepartmentNotFoundException {
        Employee existingEmployee = employeeRepo.findById(empDTO.getEmpId()).orElseThrow(() -> new EmployeeNotFoundException());;
        Optional<Employee> managerOpt = employeeRepo.findById(empDTO.getSupervisorId());
		Employee manager = managerOpt.isPresent() ? managerOpt.get() : null;
		if (empDTO.getDeptId() != null) {
			Department dept = deptRepo.findById(empDTO.getDeptId()).orElseThrow(() -> new DepartmentNotFoundException());
				Employee updateEmployee = employeeMapper.mapEmployeeDtoToEmployee(existingEmployee.getCorporate(),empDTO);
				updateEmployee.setIs_Poc(existingEmployee.getIs_Poc());
				updateEmployee.setDept(dept);
				updateEmployee.setManager(manager);
				updateEmployee.setCreatedBy(existingEmployee.getCreatedBy());
				updateEmployee.setCreatedOn(existingEmployee.getCreatedOn());
				updateEmployee.setUpdatedBy(empDTO.getUpdatedByEmail());
				updateEmployee.setUpdatedOn(new java.util.Date());
				updateEmployee.setEmpId(existingEmployee.getEmpId());
				updateEmployee.setIsActive(existingEmployee.getIsActive());
				return employeeRepo.save(updateEmployee);
		}else {
			throw new EcoatsException("Department is required");
		}
    }

    @Override
    public void deleteEmployee(long employeeId, String emailAddr) throws EmployeeNotFoundException {
        Optional<Employee> existingEmployee = employeeRepo.findById(employeeId);
        if(existingEmployee.isPresent()){
            existingEmployee.get().setIsActive(0);
            existingEmployee.get().setUpdatedBy(emailAddr);
            existingEmployee.get().setUpdatedOn(new java.util.Date());
            employeeRepo.save(existingEmployee.get());
        }else {
            throw new EmployeeNotFoundException();
        }
    }

    private void saveEmployeeTSSettings(Long corpId,Employee emp, String createdBy){
        try{
            log.info("--inside saveEmployeeTSSettings method--");
            CorporateTimesheetSettings corporateTimesheetSettings = corporateTimesheetSettingsRepo.findByCorporateId(corpId);
            if(null != corporateTimesheetSettings){
                EmployeeTimesheetSettings employeeTimesheetSettings = EmployeeTimesheetSettings.builder().employee(emp)
                        .corporateTimesheetSettings(corporateTimesheetSettings)
                        .isActive(1).createdBy(createdBy).createdOn(new Date()).build();
                employeeTimesheetSettingsRepo.save(employeeTimesheetSettings);
            }else{
                log.info("--corporateTimesheetSettings not found--");
            }
            log.info("--saved Employee TS Settings--");
        }catch(Exception e){
            log.error("Exception occurred in saveEmployeeTSSettings:"+e.getMessage());
        }

    }
}
