package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tbl_user")
public class User implements Serializable {
	@Id
	@Column(name = "user_id")
	private String userId;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "corporate_id") // , referencedColumnName = "corporate_id"
	private Corporate corporate;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id") // , referencedColumnName = "employee_id"
	private Employee employee;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "tbl_role_user", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	@JsonIgnore
	private String password;
	private Integer retries;
	@Column(name = "effective_date")
	private Date effDate;
	@Column(name = "expiration_date")
	private Date expDate;
	private Integer locked;
	@Column(name = "is_active")
	private Integer isActive;
	@JsonIgnore
	@Column(name = "created_by")
	private String createdBy;
	@JsonIgnore
	@Column(name = "created_on")
	private Date createdOn;
	@JsonIgnore
	@Column(name = "updated_by")
	private String updatedBy;
	@JsonIgnore
	@Column(name = "updated_on")
	private Date updatedOn;

}
