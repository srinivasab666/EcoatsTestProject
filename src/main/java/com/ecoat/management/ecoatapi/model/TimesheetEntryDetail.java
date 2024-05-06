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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tbl_timesheet_entry_detail")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//property = "timesheetEntryDetailId")
public class TimesheetEntryDetail implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3827195413613941331L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_entry_detail_id")
    private long timesheetEntryDetailId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "timesheet_entry_id")
    @JsonBackReference
    private TimesheetEntry timesheetEntry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name = "leave_id")
    private TimeSheetLeaveTypes timeSheetLeaveTypes;
    
    @Column(name = "service_name")
    private String serviceName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "date")
    private Date entryDate;

    @Column(name = "hours")
    private float time;

    @Column(name = "additional_comments")
    private String additionalComments;

    @Column(name = "comments_tag")
    private String commentsTag;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on",updatable = false)
//    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on",updatable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
    
    @Column(name = "is_billable")
    private boolean isBillable;
}
