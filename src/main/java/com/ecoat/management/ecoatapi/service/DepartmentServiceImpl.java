package com.ecoat.management.ecoatapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ecoat.management.ecoatapi.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecoat.management.ecoatapi.dto.DepartmentDTO;
import com.ecoat.management.ecoatapi.dto.UpdateClientAddressReqDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.DepartmentAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.DepartmentNotFoundException;
import com.ecoat.management.ecoatapi.mapper.DepartmentMapper;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Department;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

	private final DepartmentRepository deptRepo;
	private final CorporateRepository corpRepo;
	private final DepartmentMapper deptMapper;
	
	@Override
	@Transactional
	public Department addDepartment(DepartmentDTO deptDto) throws CorporateNotFoundException, DepartmentAlreadyExistsException {
		// TODO Auto-generated method stub
		Corporate corp = null;
		Department updatedDept = null;
		List<Department> existingDepts = deptRepo.findAllByCorporate_Id(deptDto.getCorpId());
		if(existingDepts.size()>0 && CommonUtil.isDuplicateDepartment(existingDepts,deptDto.getDeptName())){
			throw new DepartmentAlreadyExistsException();
		}else{
			corp = corpRepo.findById(deptDto.getCorpId())
					.orElseThrow(() -> new CorporateNotFoundException());
			Department dept = deptMapper.mapDeptDtoToDepartment(corp, deptDto);
			dept.setCreatedBy(deptDto.getUserId());
			dept.setCreatedOn(new Date());
			updatedDept = deptRepo.saveAndFlush(dept);
		}
		return updatedDept;
	}

	@Override
	public Department getDepartment(Long deptId) throws DepartmentNotFoundException {
		Optional<Department> existingDept = deptRepo.findById(deptId);
		if (existingDept.isPresent()) {
			log.info("Department fetched successfully..." + deptId);
			return existingDept.get();
		} else {
			log.info("Department does not not exist..." + deptId);
			throw new DepartmentNotFoundException();
		}
	}

	@Override
	public void deleteDepartment(Long deptId,String userId) throws DepartmentNotFoundException {
		Optional<Department> existingDept = deptRepo.findById(deptId);
		if (existingDept.isPresent()) {
			Department dept = existingDept.get();
			dept.setIsActive(0);
			dept.setUpdatedBy(userId);
			deptRepo.saveAndFlush(dept);
			log.info("Department deleted successfully..." + deptId);
		} else {
			log.error("Department does not not exist..." + deptId);
			throw new DepartmentNotFoundException();
		}
		
	}

	@Override
	public List<Department> getDepartementsByCorp(Long corpId) throws DepartmentNotFoundException {
		return deptRepo.findAllByCorporate_Id(corpId);
	}

	@Override
	public List<Department> getDepartments() {
		List<Department> deptList = deptRepo.findAll();
		return deptList;
	}

	@Override
	public Department editDepartment(DepartmentDTO deptDto) throws CorporateNotFoundException, DepartmentNotFoundException {
		Department updatedDept = null;
		Optional<Department> existingDept = deptRepo.findById(deptDto.getDeptId());
		if (existingDept.isPresent()) {
			Department dept = existingDept.get();
			Corporate corp = corpRepo.findById(deptDto.getCorpId())
					.orElseThrow(() -> new CorporateNotFoundException());
			updatedDept = deptMapper.mapDeptDtoToDepartment(corp, deptDto);
			updatedDept.setUpdatedBy(deptDto.getUserId());
			updatedDept.setDeptId(dept.getDeptId());
			updatedDept = deptRepo.saveAndFlush(updatedDept);
		} else {
			log.info("Department does not not exist..." + deptDto.getDeptName());
			throw new DepartmentNotFoundException();
		}
		return updatedDept;
	}

}
