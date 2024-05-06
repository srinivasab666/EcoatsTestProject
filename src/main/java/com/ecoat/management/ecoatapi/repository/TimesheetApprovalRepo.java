package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecoat.management.ecoatapi.enums.TimeSheetApprovalStatusCodeEnum;
import com.ecoat.management.ecoatapi.model.TimesheetApprovalStatusCode;
import com.ecoat.management.ecoatapi.model.TimesheetEntryApproval;

public interface TimesheetApprovalRepo extends JpaRepository<TimesheetEntryApproval, Long>   {

	
	@Query("select tasc from TimesheetApprovalStatusCode tasc where tasc.statusCode = :approvalStatus and tasc.isActive = 1")
	Optional<TimesheetApprovalStatusCode> findByApprovalStatus(@Param("approvalStatus") TimeSheetApprovalStatusCodeEnum approvalStatus);

	@Query(value = "select * from tbl_timesheet_entry_approval where approver_id=?1 and is_active = 1", nativeQuery = true)
	Optional<List<TimesheetEntryApproval>> getAllTimesheetEntryApprovalByApproverId(long id);
	
	@Query(value = "select ta from TimesheetEntryApproval ta where ta.timesheetEntry.timesheetEntryId=?1 and ta.isActive = 1 order by timesheetEntryApprovalId desc")
	Optional<List<TimesheetEntryApproval>> getAllActiveTimesheetEntryApprovalByEntryId(long entryid);
	
	@Query(value = "select ta from TimesheetEntryApproval ta where ta.timesheetEntry.timesheetEntryId=?1 order by timesheetEntryApprovalId desc")
	Optional<List<TimesheetEntryApproval>> getAllTimesheetEntryApprovalByEntryId(long entryid);
}
