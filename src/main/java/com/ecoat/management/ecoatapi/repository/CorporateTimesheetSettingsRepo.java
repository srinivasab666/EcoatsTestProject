package com.ecoat.management.ecoatapi.repository;

import com.ecoat.management.ecoatapi.model.CorporateTimesheetSettings;
import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorporateTimesheetSettingsRepo extends JpaRepository<CorporateTimesheetSettings, Long> {

    @Query(value = "select * from tbl_corprate_timesheet_setting where corporate_id=?1",nativeQuery = true)
    CorporateTimesheetSettings findByCorporateId(long corpId);

    @Query(value = "select * from tbl_corprate_timesheet_setting where corporate_id=?1 and timesheet_type=?2",nativeQuery = true)
    Optional<CorporateTimesheetSettings> findByCorporateIdAndTimesheetType(long corpId, String timesheetType);
}
