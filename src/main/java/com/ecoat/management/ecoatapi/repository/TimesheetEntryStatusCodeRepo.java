package com.ecoat.management.ecoatapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.enums.TimesheetEntryStatusCodeEnum;
import com.ecoat.management.ecoatapi.model.TimesheetEntryStatusCode;

@Repository
public interface TimesheetEntryStatusCodeRepo extends JpaRepository<TimesheetEntryStatusCode,String> {
	
	@Query("Select t from TimesheetEntryStatusCode t where t.statusCode = :statusCode")
	Optional<TimesheetEntryStatusCode> findByStatusCode(@Param("statusCode") TimesheetEntryStatusCodeEnum statusCode);
	
}
