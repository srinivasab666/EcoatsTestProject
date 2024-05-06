package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateTimesheetSettingDTO {

    private static final long serialVersionUID = 3617754814801947334L;

    private Long timesheetSettingIid;

    @Min(value = 1,message = "corpId can't be less than 1")
    private Long corpId;

    private String timesheetType;

    private String createdBy;

    private String updatedBy;

}
