package com.ecoat.management.ecoatapi.repository;

import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeTimesheetSettingsRepo extends JpaRepository<EmployeeTimesheetSettings, Long> {

    @Query(value = "select * from tbl_employee_timesheet_setting where employee_id=?1",nativeQuery = true)
    Optional<EmployeeTimesheetSettings> findByEmployeeId(long empId);
}
