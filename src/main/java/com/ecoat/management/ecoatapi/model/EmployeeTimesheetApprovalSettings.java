package com.ecoat.management.ecoatapi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tbl_employee_timesheet_approval_settings")
public class EmployeeTimesheetApprovalSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_approval_settings_id")
    private Long timesheetApprovalSettingsId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "timesheet_setting_id")
    private EmployeeTimesheetSettings timesheetSettings;

    @Column(name = "approval_level")
    private int approvalLevel;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "approver_id")
    private Employee employee;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
}
