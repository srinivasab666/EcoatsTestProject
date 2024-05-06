package com.ecoat.management.ecoatapi.model;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_department")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "department_id")
	private long deptId;
	@Column(name = "department_name")
	private String deptName;
	@Column(name = "department_location")
	private String deptLocation;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "corporate_id")
	private Corporate corporate;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_on")
	private Date createdOn;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_on",insertable = false,updatable = false)
	private Date updatedOn;
	@Column(name = "is_active")
	private Integer isActive;
}
