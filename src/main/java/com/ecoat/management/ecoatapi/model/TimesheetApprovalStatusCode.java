package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ecoat.management.ecoatapi.enums.TimeSheetApprovalStatusCodeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_timesheet_approval_status_code")
public class TimesheetApprovalStatusCode implements Serializable {

    @Id
    @Column(name = "status_code")
    @Enumerated(EnumType.STRING)
    private TimeSheetApprovalStatusCodeEnum statusCode;

    @Column(name = "description")
    private String description;

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
