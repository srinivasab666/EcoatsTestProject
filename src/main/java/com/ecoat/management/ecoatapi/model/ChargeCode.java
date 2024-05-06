package com.ecoat.management.ecoatapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_charge_code")
public class ChargeCode implements Serializable {
    @Id
    @Column(name = "charge_code_id")
    private String chargeCodeId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "change_code_name")
    private String chargeCodeName;
    @Column(name = "effective_date")
    private Date effectiveDate;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "corporate_id")
    private long corporateId;
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
