package com.ecoat.management.ecoatapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_employee_billing")
@Builder
public class EmployeeBilling implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_billing_id")
    private long employeeBillingId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "billing_rate")
    private Double billingRate;

    @Column(name = "billing_type")
    private String billingType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
}
