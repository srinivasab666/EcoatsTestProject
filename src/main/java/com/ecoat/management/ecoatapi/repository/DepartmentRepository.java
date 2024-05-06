package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findByDeptName(String deptName);

	@Query(value="select * from tbl_department where corporate_id =?1",nativeQuery = true)
	List<Department> findAllByCorporate_Id(long corpId);
}
