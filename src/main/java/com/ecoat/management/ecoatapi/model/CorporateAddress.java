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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_corporate_address")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CorporateAddress implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7353374180430282326L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="corpr_corporate_id")
    private long corpAddressid;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "corporate_id", nullable = false)  // referencedColumnName = "corporate_id"
    @JsonBackReference
    private Corporate corporate;
    @Column(name="address_1")
    private String address1;
    @Column(name="address_2")
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String fax;
    private String website;
    @Column(name="created_by")
    private String createdBy;
    @Column(name="created_on",updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_by")
    private String updatedBy;
    @Column(name="updated_on",updatable = false)
    private Date updatedOn;
    @Column(name="is_active")
    private Integer isActive;

    
}
