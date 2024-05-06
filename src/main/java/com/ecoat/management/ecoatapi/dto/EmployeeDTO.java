package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 3617754814801947335L;
    private Long empId;
    private Long corporateId;
    private String employeeCode;
    @NotEmpty(message="firstName cannot be null")
    private String firstName;
    @NotEmpty(message="lastName cannot be null")
    private String lastName;
    @NotEmpty(message="emailAddress cannot be null")
    private String emailAddress;
    @Min(value = 1,message = "deptId can't be less than 1")
    private Long deptId;
    private long supervisorId;
    private String createdByEmail;
    private String updatedByEmail;
    private Boolean isPOC;
}
