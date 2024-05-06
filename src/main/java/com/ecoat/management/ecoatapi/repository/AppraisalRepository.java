package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecoat.management.ecoatapi.model.AppraisalCategories;
import com.ecoat.management.ecoatapi.model.AppraisalRating;

public interface AppraisalRepository extends JpaRepository<AppraisalRating,Long>  {

	@Query("select ac from AppraisalRating ac where ac.categories.id = :categoryId and ac.employee.empId = :empId and ac.startYear = :startYear and ac.endYear = :endYear and ac.isActive = 1 order by ac.id desc")
	Optional<AppraisalRating> findByEmployeeAndCategory(Long empId, Long categoryId, Long startYear, Long endYear);

	@Query("select ac from AppraisalRating ac where ac.employee.empId = :empId and ac.startYear = :startYear and ac.endYear = :endYear and ac.isActive = 1 order by ac.id desc")
	Optional<List<AppraisalRating>> findAppraisalByEmpIdAndStartAndEndYears(Long empId, Long startYear, Long endYear);
	
	
}
