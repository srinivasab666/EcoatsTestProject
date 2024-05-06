package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetApprovalSettingDTO {

    private static final long serialVersionUID = 3617754814801947334L;

    @Min(value = 1,message = "timesheetSettingIid can't be less than 1")
    private Long timesheetSettingIid;

    @Min(value = 1,message = "timesheetSettingIid can't be less than 1")
    private Integer approvalLevel;

    @Min(value = 1,message = "empId can't be less than 1")
    private Long approverId;

    private String createdBy;

    private String updatedBy;

}
