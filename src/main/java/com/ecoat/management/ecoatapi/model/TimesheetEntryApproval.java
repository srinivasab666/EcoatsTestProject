package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_timesheet_entry_approval")
public class TimesheetEntryApproval implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "timesheet_entry_approval_id")
	private long timesheetEntryApprovalId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "timesheet_entry_id")
	private TimesheetEntry timesheetEntry;

	@Column(name = "approval_level")
	private int approvalLevel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "approver_id")
	private Employee approver;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "approver_by_id")
	private Employee approverByIdDtls;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "comments")
	private String comments;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "status_code")
	private TimesheetApprovalStatusCode timesheetApprovalStatusCode;
	
	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_on",updatable = false)
	private Date createdOn;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_on",updatable = false)
	@UpdateTimestamp
	private Date updatedOn;

	@Column(name = "is_active")
	private int isActive;
}
