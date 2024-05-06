package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "tbl_timesheet_entry")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//property = "timesheetEntryId")
public class TimesheetEntry implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7549818816090922619L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_entry_id")
    private long timesheetEntryId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "timesheet_setting_id")
    @JsonBackReference
    private EmployeeTimesheetSettings timesheetSettings;

    @Column(name = "from_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @Column(name = "to_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    @Column(name = "submitted_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date submittedDate;

    @Column(name = "final_approved_date")
    private Date finalApprovedDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_code")
    @JsonBackReference
    private TimesheetEntryStatusCode statusCode;

    @Column(name = "previous_timesheet_entry_id")
    private long previousTimesheetEntryId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on" ,updatable = false)
    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
    
    @Column(name = "appriasal_status_comments")
    private String appraisalComments;
    
    @Column(name = "is_appraisal_include")
    private boolean isAppraisalInclude;
    
//    @OneToMany(mappedBy = "timesheetEntry", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<TimesheetEntryDetail> timesheetEntryDetail;
    
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "timesheetEntry",cascade = CascadeType.ALL)
    private List<TimesheetEntryDetail> timesheetEntryDetails;
}
