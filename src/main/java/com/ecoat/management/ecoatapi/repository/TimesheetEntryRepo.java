package com.ecoat.management.ecoatapi.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.TimesheetEntry;

@Repository
public interface TimesheetEntryRepo extends JpaRepository<TimesheetEntry,Long> {

    @Query(value="select * from tbl_timesheet_entry where timesheet_setting_id=?1 order by from_date desc limit 10",nativeQuery = true)
    List<TimesheetEntry> findByTimesheetSettingId(long timesheetSettingId);
    
    @Query(value = "select td from TimesheetEntry td where td.timesheetEntryId = :timesheetEntryId and td.isActive = 1")
	Optional<TimesheetEntry> findEntryById(Long timesheetEntryId);
    
    @Query(value="select te from TimesheetEntry te join EmployeeTimesheetSettings ts on te.timesheetSettings.timesheetSettingId = ts.timesheetSettingId and te.isActive = 1 and ts.employee.empId = :empId")
	List<TimesheetEntry> findTimesheetEntriesByEmpId(long empId);

    @Query(value="select te.* from tbl_timesheet_entry te join tbl_employee_timesheet_setting ts\n" +
            "on te.timesheet_setting_id=ts.timesheet_setting_id \n" +
            "and ts.employee_id=?1 \n" +
            "and ?2 between te.from_date and te.to_date" , nativeQuery = true)
    TimesheetEntry findTimesheetEntryByDate(long empId, Date date);

    @Query(value="select te.* from tbl_timesheet_entry_detail td join tbl_timesheet_entry te join tbl_employee_timesheet_setting ts on te.timesheet_setting_id=ts.timesheet_setting_id and te.timesheet_entry_id = td.timesheet_entry_id and ts.employee_id=?1 and td.date between ?2 and ?3 order by from_date desc",
            nativeQuery = true)
    List<TimesheetEntry> findTimesheetEntryByFromDateRange(long empId, Date fromDate, Date toDate);
    
    @Query(value="select te from TimesheetEntry te join EmployeeTimesheetSettings ts on te.isAppraisalInclude=true and te.timesheetSettings.timesheetSettingId = ts.timesheetSettingId and te.isActive = 1 and ts.employee.empId = :empId and te.fromDate >= :fromDate and te.toDate <= :toDate")
    List<TimesheetEntry> getAllActiveTimesheetsByDate(long empId,Date fromDate,Date toDate);

}
