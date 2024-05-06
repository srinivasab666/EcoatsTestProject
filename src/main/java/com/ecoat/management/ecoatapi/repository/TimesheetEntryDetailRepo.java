package com.ecoat.management.ecoatapi.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.enums.LeavesEnum;
import com.ecoat.management.ecoatapi.enums.TimeSheetApprovalStatusCodeEnum;
import com.ecoat.management.ecoatapi.model.TimeSheetLeaveTypes;
import com.ecoat.management.ecoatapi.model.TimesheetApprovalStatusCode;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.TimesheetEntryDetail;

@Repository
public interface TimesheetEntryDetailRepo extends JpaRepository<TimesheetEntryDetail,Long> {

	@Query(value = "select td from TimesheetEntryDetail td where td.isActive = 1 and td.timesheetEntry.timesheetEntryId = :timesheetEntryId order by td.timesheetEntryDetailId desc")
	List<TimesheetEntryDetail> findByEntryId(Long timesheetEntryId);
	
	@Query(value="select td.* from tbl_timesheet_entry_detail td join tbl_timesheet_entry te join tbl_employee_timesheet_setting ts on td.timesheet_entry_id = te.timesheet_entry_id and "
			+ "te.timesheet_setting_id = ts.timesheet_setting_id and td.is_active = 1 and ts.employee_id = :empId",nativeQuery = true)
	List<TimesheetEntryDetail> findByEmployeeId(long empId);
	
	@Modifying
	@Query("delete from TimesheetEntryDetail td where td.timesheetEntryDetailId = :timesheetEntryId")
	void deleteTimeSheetById(long timesheetEntryId);
	
	
	@Query("select tl from TimeSheetLeaveTypes tl where tl.leaveName = :leavesEnum and tl.isActive = 1")
	Optional<TimeSheetLeaveTypes> findByLeaveName(LeavesEnum leavesEnum);

	@Query(value="select td from TimesheetEntryDetail td where td.timesheetEntry.timesheetEntryId in (:entryIds) and td.entryDate between :fromDate and :toDate")
	List<TimesheetEntryDetail> findByCustomDates(List<Long> entryIds, Date fromDate, Date toDate);
	
}
