package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeCodeDTO implements Serializable {
    private String chargeCdId;
    @NotNull(message = "projectId cannot be null")
    private long projectId;
    @NotEmpty(message = "chargeCodeName cannot be empty")
    private String chargeCodeName;
    private Date effectiveDate;
    private Date expirationDate;
    @NotNull(message = "corporateId cannot be null")
    private long corporateId;
    private String createdByEmail;
    private String updatedByEmail;
}
