package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


//@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_client_address")
public class ClientAddress implements Serializable{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_address_id")
    private long clientAddressId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;
    @Column(name = "address_1")
    private String clientAddress1;
    @Column(name = "address_2")
    private String clientAddress2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String fax;
    private String website;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_on")
    private Date createdOn;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_on")
    private Date updatedOn;
    @Column(name = "is_active")
    private Integer isActive;
}
