package com.ecoat.management.ecoatapi.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_project")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private long projectId;
	@Column(name = "project_name")
	private String projectName;
	@Column(name = "project_description")
	private String projectDescription;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "client_id")
	private Client client;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "corporate_id")
	@JsonIgnore
	private Corporate corporate;
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
