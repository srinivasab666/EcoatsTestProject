package com.ecoat.management.ecoatapi.repository;

import com.ecoat.management.ecoatapi.model.EmployeeTimesheetApprovalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeTimesheetApprovalSettingsRepo extends JpaRepository<EmployeeTimesheetApprovalSettings, Long> {
    @Query(value = "select * from tbl_employee_timesheet_approval_settings where timesheet_setting_id=?1",nativeQuery = true)
    Optional<EmployeeTimesheetApprovalSettings> findByTimesheetSettingId(long id);
}
