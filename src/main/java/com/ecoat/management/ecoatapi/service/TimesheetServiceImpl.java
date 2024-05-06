package com.ecoat.management.ecoatapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ecoat.management.ecoatapi.dto.CorporateTimesheetSettingDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetApproveDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetDTO;
import com.ecoat.management.ecoatapi.dto.TimesheetSettingDTO;
import com.ecoat.management.ecoatapi.dto.UpdateTimeSheetDTO;
import com.ecoat.management.ecoatapi.enums.LeavesEnum;
import com.ecoat.management.ecoatapi.enums.TimeSheetApprovalStatusCodeEnum;
import com.ecoat.management.ecoatapi.enums.TimesheetEntryStatusCodeEnum;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.exception.EmployeeNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.mapper.TimesheetDTOMapper;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateTimesheetSettings;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeTimesheetSettings;
import com.ecoat.management.ecoatapi.model.Project;
import com.ecoat.management.ecoatapi.model.TimeSheetLeaveTypes;
import com.ecoat.management.ecoatapi.model.TimesheetApprovalStatusCode;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.TimesheetEntryApproval;
import com.ecoat.management.ecoatapi.model.TimesheetEntryDetail;
import com.ecoat.management.ecoatapi.model.TimesheetEntryStatusCode;
import com.ecoat.management.ecoatapi.model.response.TimeSheetDetailsListResponse;
import com.ecoat.management.ecoatapi.repository.ClientRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.CorporateTimesheetSettingsRepo;
import com.ecoat.management.ecoatapi.repository.EmployeeRepository;
import com.ecoat.management.ecoatapi.repository.EmployeeTimesheetApprovalSettingsRepo;
import com.ecoat.management.ecoatapi.repository.EmployeeTimesheetSettingsRepo;
import com.ecoat.management.ecoatapi.repository.ProjectRepository;
import com.ecoat.management.ecoatapi.repository.TimesheetApprovalRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryDetailRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryRepo;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryStatusCodeRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService {

	private final EmployeeTimesheetSettingsRepo empTimesheetStngsRepo;
	private final CorporateTimesheetSettingsRepo corporateTimesheetSettingsRepo;
	private final EmployeeTimesheetApprovalSettingsRepo employeeTimesheetApprovalSettingsRepo;
	private final TimesheetEntryDetailRepo timesheetEntryDetailRepo;
	private final TimesheetEntryStatusCodeRepo timesheetEntryStatusCodeRepo;
	private final ProjectRepository projectRepository;
	private final ClientRepository clientRepository;
	private final TimesheetApprovalRepo timesheetApprovalRepo;
	private final EmployeeRepository employeeRepository;
	private final CorporateRepository corpRepo;
	private final TimesheetEntryRepo timesheetEntryRepo;

	@Override
	@Transactional
	public List<TimesheetEntryDetail> submitTimesheet(TimesheetDTO timesheetDTO) throws ProjectNotFoundException, ClientNotFoundException {
		String method = "TimesheetServiceImpl.submitTimesheet ";
		log.info(method + "Enter");
		Optional<TimesheetEntryStatusCode> timesheetEntryStatusCode = null;
		List<TimesheetEntryDetail> timesheetEntryDetails = new ArrayList<>();
		List<TimesheetEntryDetail> savedTimesheetEntryDetails = new ArrayList<>();

		Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo
				.findByEmployeeId(timesheetDTO.getEmpId());
		timesheetEntryStatusCode = getEntryStatusCode(timesheetDTO.isSubmitted());
		TimesheetEntry timesheetEntry = TimesheetDTOMapper.populateTimesheetEntry(timesheetDTO,
				employeeTimesheetSettings.get(), timesheetEntryStatusCode.get());

		timesheetDTO.getEntryDtlsList().stream().forEach((entrydtls) -> {
			Project project = projectRepository.findById(entrydtls.getProjectId()).orElse(null);
			Client client  = clientRepository.findById(entrydtls.getClientId()).orElseThrow(() -> new ClientNotFoundException());
				
			TimesheetEntryDetail timesheetEntryDetail = TimesheetDTOMapper.populateTimesheetEntryDetail(timesheetDTO,
					timesheetEntry, project, client, entrydtls);
			
			if (StringUtils.hasText(entrydtls.getTimeoff())  ) {
				TimeSheetLeaveTypes tlType = timesheetEntryDetailRepo.findByLeaveName(LeavesEnum.find(entrydtls.getTimeoff())).orElseThrow(() -> new EcoatsException("Leave type is not found"));
				timesheetEntryDetail.setTimeSheetLeaveTypes(tlType);
			}
			timesheetEntryDetails.add(timesheetEntryDetail);
		});
		savedTimesheetEntryDetails = timesheetEntryDetailRepo.saveAll(timesheetEntryDetails);
		saveTimeSheetApproval(timesheetEntry, employeeTimesheetSettings.get().getEmployee(),
				TimeSheetApprovalStatusCodeEnum.PENDING, timesheetDTO.getCommentsTag());
		log.info(method + "Exit");
		return savedTimesheetEntryDetails;
	}

	private Optional<TimesheetEntryStatusCode> getEntryStatusCode(boolean isSubmitted) {
		Optional<TimesheetEntryStatusCode> timesheetEntryStatusCode;
		if (isSubmitted) {
			timesheetEntryStatusCode = timesheetEntryStatusCodeRepo
					.findByStatusCode(TimesheetEntryStatusCodeEnum.SUBMITTED);
		} else {
			timesheetEntryStatusCode = timesheetEntryStatusCodeRepo
					.findByStatusCode(TimesheetEntryStatusCodeEnum.SAVED);
		}
		return timesheetEntryStatusCode;
	}

	private TimesheetEntryApproval saveTimeSheetApproval(TimesheetEntry timesheetEntry, Employee employee,
			TimeSheetApprovalStatusCodeEnum status, String comment) {
		String method = "TimesheetServiceImpl.saveTimeSheetApproval ";
		log.info(method + "Enter");
		Employee approver = null;
		if (employee.getManager() == null) {
//			throw new EcoatsException("Manager should not be null");
			approver = employee;
		}else {
			approver = employee.getManager();
		}
		TimesheetApprovalStatusCode optStatus = timesheetApprovalRepo.findByApprovalStatus(status)
				.orElseThrow(() -> new EcoatsException("approval status not found"));
		TimesheetEntryApproval approval = TimesheetEntryApproval.builder().approvalLevel(1)
				.approver(approver).approverByIdDtls(approver)
				.comments(comment).createdBy(employee.getEmail())
				.timesheetApprovalStatusCode(optStatus).timesheetEntry(timesheetEntry).isActive(1).createdOn(new Date()).build();
		TimesheetEntryApproval savedApproval = timesheetApprovalRepo.save(approval);
		log.info(method + "Exit");
		return savedApproval;
	}

	@Override
	@Transactional
	public List<TimesheetEntryDetail> updateTimesheet(UpdateTimeSheetDTO timesheetDTO) {
		String method = "TimesheetServiceImpl.updateTimesheet ";
		log.info(method + "Enter");
		List<TimesheetEntryDetail> timesheetEntryDetails = new ArrayList<>();
		
		List<TimesheetEntryDetail> savedTimesheetEntryDetails = new ArrayList<>();
		EmployeeTimesheetSettings employeeTimesheetSettings = empTimesheetStngsRepo.findByEmployeeId(timesheetDTO.getEmpId()).orElseThrow(()-> new EcoatsException("employee not having any timesheet settings"));
		Optional<TimesheetEntryStatusCode> timesheetEntryStatusCode = getEntryStatusCode(timesheetDTO.isSubmitted());
		Employee employee = employeeTimesheetSettings.getEmployee();
		TimesheetEntry tsEntry = timesheetEntryRepo.findEntryById(timesheetDTO.getTimesheetEntryId()).orElseThrow(()-> new EcoatsException("Timesheet entry is not available"));
		timesheetDTO.getEntryDtlsList().stream().forEach((req) -> {
			TimeSheetLeaveTypes tlType = null;
			if (StringUtils.hasText(req.getTimeoff())  ) {
				tlType = timesheetEntryDetailRepo.findByLeaveName(LeavesEnum.find(req.getTimeoff())).orElseThrow(() -> new EcoatsException("Leave type is not found"));
			}
			Optional<TimesheetEntryDetail> optTimesheetDtls = timesheetEntryDetailRepo.findById(req.getTimesheetEntryDtsId());
			tsEntry.setTimesheetSettings(employeeTimesheetSettings);
			tsEntry.setStatusCode(timesheetEntryStatusCode.get());
			tsEntry.setAppraisalComments(timesheetDTO.getAppraisalStatusComments());
			tsEntry.setAppraisalInclude(timesheetDTO.isAppraisalInclude());
			if (optTimesheetDtls.isPresent()) {
				TimesheetEntryDetail ts = optTimesheetDtls.get();
				ts.setEntryDate(req.getEntryDate());
				ts.setTime(req.getEntryHours());
				ts.setServiceName(req.getServiceName());
				ts.setTimesheetEntry(tsEntry);
				ts.setTimeSheetLeaveTypes(tlType);
				Client client  = clientRepository.findById(req.getClientId()).orElseThrow(() -> new ClientNotFoundException());
				Project project = projectRepository.findById(req.getProjectId()).orElse(null);
				ts.setClient(client);
				ts.setProject(project);
				ts.setUpdatedBy(employee.getEmail());
				ts.setBillable(req.getIsBillable());
				timesheetEntryDetails.add(ts);
			}else {
				Client client  = clientRepository.findById(req.getClientId()).orElseThrow(() -> new ClientNotFoundException());
				Project project = projectRepository.findById(req.getProjectId()).orElse(null);
				TimesheetEntryDetail timesheetEntryDetail = TimesheetEntryDetail.builder().timesheetEntry(tsEntry)
						.project(project).client(client).entryDate(req.getEntryDate()).time(req.getEntryHours()).isBillable(req.getIsBillable())
						.isActive(1).createdBy(employee.getEmail()).updatedBy(employee.getEmail())
						.serviceName(req.getServiceName()).timeSheetLeaveTypes(tlType).createdOn(new Date()).updatedOn(new Date()).build();
				timesheetEntryDetails.add(timesheetEntryDetail);
			}
		});
		savedTimesheetEntryDetails = timesheetEntryDetailRepo.saveAll(timesheetEntryDetails);
		Optional<List<TimesheetEntryApproval>> tsApprovalListOpt = timesheetApprovalRepo.getAllTimesheetEntryApprovalByEntryId(tsEntry.getTimesheetEntryId());
		if (tsApprovalListOpt.isPresent() && !tsApprovalListOpt.get().isEmpty()) {
			TimesheetEntryApproval app = tsApprovalListOpt.get().get(0);
			TimesheetApprovalStatusCode optStatus = timesheetApprovalRepo.findByApprovalStatus(TimeSheetApprovalStatusCodeEnum.PENDING)
					.orElseThrow(() -> new EcoatsException("approval status not found"));
			app.setTimesheetApprovalStatusCode(optStatus);
			app.setIsActive(1);
			timesheetApprovalRepo.save(app);
		} else {
			saveTimeSheetApproval(tsEntry, employee,
					TimeSheetApprovalStatusCodeEnum.PENDING, null);
		}
		log.info(method + "Exit");
		return savedTimesheetEntryDetails;
	}

	@Override
	public List<TimeSheetDetailsListResponse> getAllEmployeeTimesheets(Long empId) {
		String method = "TimesheetServiceImpl.getAllEmployeeTimesheets ";
		List<TimeSheetDetailsListResponse> response = new ArrayList<TimeSheetDetailsListResponse>();
		log.info(method + "Enter");
		List<TimesheetEntry> entriesList = timesheetEntryRepo.findTimesheetEntriesByEmpId(empId);
		
		if (entriesList != null && entriesList.size() > 0 ) {
			entriesList.stream().forEach((te)->{
				Optional<List<TimesheetEntryApproval>>  optApprovalList = timesheetApprovalRepo.
						getAllActiveTimesheetEntryApprovalByEntryId(te.getTimesheetEntryId());
				if (optApprovalList.isPresent()) {
					List<TimesheetEntryApproval> approvalList = optApprovalList.get();
					if (approvalList.size() > 0 ) {
						TimeSheetDetailsListResponse tlr = new TimeSheetDetailsListResponse();
						TimesheetEntryApproval ta = approvalList.get(0);
						String approvalStatus1 = ta.getTimesheetApprovalStatusCode().getStatusCode().getStatusCode();
						tlr.setApprovalStatus(approvalStatus1);
						tlr.setTimesheetEntry(te);
						response.add(tlr);
					}
				}
				
			});
		}
		log.info(method + "Exit");
		return response;
	}

	@Override
	public List<TimesheetEntryDetail> getAllTimesheetsByApproverId(Long empId) {
		String method = "TimesheetServiceImpl.getAllTimesheetsByApproverId ";
		log.info(method + "Enter");
		List<TimesheetEntryDetail> timehseets = new ArrayList<>();
		Optional<List<TimesheetEntryApproval>> tsApprovals = timesheetApprovalRepo.getAllTimesheetEntryApprovalByApproverId(empId);
		if(tsApprovals.isPresent()){
			tsApprovals.get().forEach(tsApproval -> {
				timehseets.addAll(timesheetEntryDetailRepo.findByEntryId(tsApproval.getTimesheetEntry().getTimesheetEntryId()));
			});
		}
		log.info(method + "Exit");
		return timehseets;
	}

	@Override
	public EmployeeTimesheetSettings addEmployeeTSSettings(TimesheetSettingDTO dto) throws EcoatsException{
		String method = "TimesheetServiceImpl.addEmployeeTSSettings ";
		log.info(method + "Enter");
		Optional<EmployeeTimesheetSettings> existingEmployeeTimesheetSettings =
				empTimesheetStngsRepo.findByEmployeeId(dto.getEmpId());
		if(existingEmployeeTimesheetSettings.isPresent()){
			throw new EcoatsException("Similar Employee Settings exists.");
		}else{
			Optional<Employee> emp = employeeRepository.findById(dto.getEmpId());
			CorporateTimesheetSettings corporateTimesheetSettings = corporateTimesheetSettingsRepo.findByCorporateId(emp.get().getCorporate().getId());
			EmployeeTimesheetSettings employeeTimesheetSettings = EmployeeTimesheetSettings.builder().employee(emp.get())
					.corporateTimesheetSettings(corporateTimesheetSettings)
					.isActive(1).createdBy(dto.getCreatedBy()).createdOn(new Date()).build();
			log.info(method + "Exit");
			return empTimesheetStngsRepo.save(employeeTimesheetSettings);
		}
	}

	@Override
	public EmployeeTimesheetSettings getEmployeeTSSettings(long id) throws EcoatsException{
		String method = "TimesheetServiceImpl.getEmployeeTSSettings ";
		log.info(method + "Enter");
		Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo.findByEmployeeId(id);
		log.info(method + "Exit");
		return employeeTimesheetSettings.get();
	}

	@Override
	public EmployeeTimesheetSettings deleteEmployeeTSSettings(long id) throws EcoatsException{
		String method = "TimesheetServiceImpl.deleteEmployeeTSSettings ";
		log.info(method + "Enter");
		Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo.findById(id);
		if(employeeTimesheetSettings.isPresent()){
			employeeTimesheetSettings.get().setIsActive(0);
			log.info(method + "Exit");
			return empTimesheetStngsRepo.save(employeeTimesheetSettings.get());
		}else{
			throw new EcoatsException("Employee Settings not found");
		}
	}

	@Override
	public CorporateTimesheetSettings addCorporateTSSettings(CorporateTimesheetSettingDTO dto) throws EcoatsException {
		String method = "TimesheetServiceImpl.addCorporateTSSettings ";
		log.info(method + "Enter");
		CorporateTimesheetSettings existingCorporateTimesheetSettings =
				corporateTimesheetSettingsRepo.findByCorporateId(dto.getCorpId());
		if(null != existingCorporateTimesheetSettings){
			throw new EcoatsException("Similar CorporateTS Settings exists.");
		}else{
			Optional<Corporate> corp = corpRepo.findById(dto.getCorpId());
			CorporateTimesheetSettings corporateTimesheetSettings = CorporateTimesheetSettings.builder().corporate(corp.get()).timesheetType(dto.getTimesheetType())
					.isActive(1).createdBy(dto.getCreatedBy()).createdOn(new Date()).build();
			corporateTimesheetSettings = corporateTimesheetSettingsRepo.save(corporateTimesheetSettings);
			Optional<List<Employee>> employees = employeeRepository.findEmployeesByCorpId(dto.getCorpId());
			if(employees.isPresent()){
				saveAllEmployeesTSSettings(employees.get(),corporateTimesheetSettings,dto.getCreatedBy());
			}
			log.info(method + "Exit");
			return corporateTimesheetSettings;
		}
	}

	private void saveAllEmployeesTSSettings(List<Employee> employees,CorporateTimesheetSettings corpTSSettings,String createdBy){
		try{
			employees.forEach(employee -> {
				EmployeeTimesheetSettings employeeTimesheetSettings = EmployeeTimesheetSettings.builder().employee(employee)
						.corporateTimesheetSettings(corpTSSettings)
						.isActive(1).createdBy(createdBy).createdOn(new Date()).build();
				empTimesheetStngsRepo.save(employeeTimesheetSettings);
			});
		}catch(Exception e){
			log.error("exception occurred in saveAllEmployeesTSSettings");
		}
	}

	@Override
	public CorporateTimesheetSettings updateCorporateTSSettings(CorporateTimesheetSettingDTO dto) throws EcoatsException {
		String method = "TimesheetServiceImpl.updateCorporateTSSettings";
		log.info(method + "Enter");
		Optional<CorporateTimesheetSettings> corporateTimesheetSettings = corporateTimesheetSettingsRepo.findById(dto.getTimesheetSettingIid());
		if(corporateTimesheetSettings.isPresent()){
			corporateTimesheetSettings.get().setTimesheetType(dto.getTimesheetType());
			corporateTimesheetSettings.get().setUpdatedBy(dto.getUpdatedBy());
			corporateTimesheetSettings.get().setUpdatedOn(new Date());
			log.info(method + "Exit");
			return corporateTimesheetSettingsRepo.save(corporateTimesheetSettings.get());
		}else{
			throw new EcoatsException("CorporateTS Settings not found");
		}
	}

	@Override
	public CorporateTimesheetSettings getCorporateTSSettings(long id) throws EcoatsException {
		String method = "TimesheetServiceImpl.getCorporateTSSettings ";
		log.info(method + "Enter");
		CorporateTimesheetSettings corporateTimesheetSettings = corporateTimesheetSettingsRepo.findByCorporateId(id);
		log.info(method + "Exit");
		return corporateTimesheetSettings;
	}

	@Override
	public CorporateTimesheetSettings deleteCorporateTSSettings(long id) throws EcoatsException {
		String method = "TimesheetServiceImpl.deleteCorporateTSSettings ";
		log.info(method + "Enter");
		Optional<CorporateTimesheetSettings> corporateTimesheetSettings = corporateTimesheetSettingsRepo.findById(id);
		if(corporateTimesheetSettings.isPresent()){
			corporateTimesheetSettings.get().setIsActive(0);
			log.info(method + "Exit");
			return corporateTimesheetSettingsRepo.save(corporateTimesheetSettings.get());
		}else{
			throw new EcoatsException("CorporateTS Settings not found");
		}
	}

//	@Override
//	public EmployeeTimesheetApprovalSettings addEmployeeTSApprovalSettings(TimesheetApprovalSettingDTO dto) throws EcoatsException{
//		Optional<EmployeeTimesheetApprovalSettings> existingEmployeeTimesheetApprovalSettings = employeeTimesheetApprovalSettingsRepo.findByTimesheetSettingId(dto.getTimesheetSettingIid());
//		if(existingEmployeeTimesheetApprovalSettings.isPresent()){
//			throw new EcoatsException("Employee TimesheetApprovalSettings already exists");
//		}else{
//			Optional<Employee> emp = employeeRepository.findById(dto.getApproverId());
//			Optional<EmployeeTimesheetSettings> employeeTimesheetSettings = empTimesheetStngsRepo.findById(dto.getTimesheetSettingIid());
//			EmployeeTimesheetApprovalSettings employeeTimesheetApprovalSettings = EmployeeTimesheetApprovalSettings.builder()
//							.timesheetSettings(employeeTimesheetSettings.get())
//							.employee(emp.get())
//							.approvalLevel(dto.getApprovalLevel())
//							.createdBy(dto.getCreatedBy())
//							.createdOn(new Date()).build();
//			return employeeTimesheetApprovalSettingsRepo.save(employeeTimesheetApprovalSettings);
//		}
//	}
//
//	@Override
//	public EmployeeTimesheetApprovalSettings updateEmployeeTSApprovalSettings(TimesheetApprovalSettingDTO dto) throws EcoatsException{
//		Optional<EmployeeTimesheetApprovalSettings> existingEmployeeTimesheetApprovalSettings = employeeTimesheetApprovalSettingsRepo.findByTimesheetSettingId(dto.getTimesheetSettingIid());
//		if(existingEmployeeTimesheetApprovalSettings.isPresent()){
//			Employee emp = employeeRepository.getById(dto.getApproverId());
//			existingEmployeeTimesheetApprovalSettings.get().setEmployee(emp);
//			existingEmployeeTimesheetApprovalSettings.get().setApprovalLevel(dto.getApprovalLevel());
//			existingEmployeeTimesheetApprovalSettings.get().setUpdatedBy(dto.getUpdatedBy());
//			existingEmployeeTimesheetApprovalSettings.get().setUpdatedOn(new Date());
//			return employeeTimesheetApprovalSettingsRepo.save(existingEmployeeTimesheetApprovalSettings.get());
//		}else{
//			throw new EcoatsException("Employee TimesheetApprovalSettings is not present");
//		}
//	}
//
//	@Override
//	public EmployeeTimesheetApprovalSettings getEmployeeTSApprovalSettings(long id) throws EcoatsException{
//		Optional<EmployeeTimesheetApprovalSettings> existingEmployeeTimesheetApprovalSettings = employeeTimesheetApprovalSettingsRepo.findByTimesheetSettingId(id);
//		return existingEmployeeTimesheetApprovalSettings.get();
//	}
//
//	@Override
//	public EmployeeTimesheetApprovalSettings deleteEmployeeTSApprovalSettings(long id) throws EcoatsException{
//		Optional<EmployeeTimesheetApprovalSettings> existingEmployeeTimesheetApprovalSettings = employeeTimesheetApprovalSettingsRepo.findByTimesheetSettingId(id);
//		if(existingEmployeeTimesheetApprovalSettings.isPresent()){
//			existingEmployeeTimesheetApprovalSettings.get().setIsActive(1);
//			return employeeTimesheetApprovalSettingsRepo.save(existingEmployeeTimesheetApprovalSettings.get());
//		}else{
//			throw new EcoatsException("Employee TimesheetApprovalSettings is not present");
//		}
//	}

	@Override
	public List<TimesheetEntryApproval> approveTimeSheet(TimesheetApproveDTO taDto) throws EmployeeNotFoundException {
		String method = "TimesheetServiceImpl.approveTimeSheet ";
		log.info(method + "Enter");
		List<TimesheetEntryApproval> finalTAList = new ArrayList<>();
		List<TimesheetEntryApproval> tsApprovalList = timesheetApprovalRepo.getAllActiveTimesheetEntryApprovalByEntryId(taDto.getTimesheetEntryId()).orElseThrow(() -> new EcoatsException("Timesheet Entry id is not valid"));
		TimeSheetApprovalStatusCodeEnum appStatus = taDto.isApproved() ? TimeSheetApprovalStatusCodeEnum.APPROVED : TimeSheetApprovalStatusCodeEnum.REJECTED;
		TimesheetApprovalStatusCode optStatus = timesheetApprovalRepo.findByApprovalStatus(appStatus)
				.orElseThrow(() -> new EcoatsException("approval status not found"));
		Employee emp = employeeRepository.findById(taDto.getApproverEmpId()).orElseThrow(() -> new EmployeeNotFoundException());
		TimesheetEntry tsEntry = timesheetEntryRepo.findEntryById(taDto.getTimesheetEntryId()).orElseThrow(()-> new EcoatsException("Timesheet entry is not available"));
		
		tsApprovalList.stream().forEach((ta) -> {
			if (taDto.isApproved()) {
				tsEntry.setFinalApprovedDate(new Date());
				ta.setIsActive(1);
			}
			tsEntry.setUpdatedBy(emp.getEmail());
			ta.setTimesheetEntry(tsEntry);
			ta.setApprovalLevel(1);
			ta.setApprovedDate(new Date());
			ta.setComments(taDto.getComments());
			ta.setTimesheetApprovalStatusCode(optStatus);
			ta.setUpdatedBy(emp.getEmail());
			finalTAList.add(timesheetApprovalRepo.save(ta));
		});
		
		log.info(method + "Exit");
		return finalTAList;
	}

	@Override
	@Transactional
	public void deleteTimeSheetDtls(List<Long> timeSheetEntryDtlsIds) {
		String method = "TimesheetServiceImpl.deleteTimeSheetDtls ";
		log.info(method + "Enter");
		List<Long> ids = new ArrayList<>(); 
		List<TimesheetEntryDetail> dtls = new ArrayList<>(); 
		try {
			timeSheetEntryDtlsIds.stream().forEach((td) -> {
				Optional<TimesheetEntryDetail> t = timesheetEntryDetailRepo.findById(td);
				if(t.isPresent()) {
//					ids.add(t.get().getTimesheetEntryDetailId());
					dtls.add(t.get());
				}
			});
			if (dtls.isEmpty()) {
				throw new EcoatsException("There is no details to delete for id: "+ timeSheetEntryDtlsIds);
			} else {
				TimesheetEntryDetail td = dtls.get(0);
				timesheetEntryDetailRepo.deleteAll(dtls);
				List<TimesheetEntryDetail> dtlsList = timesheetEntryDetailRepo.findByEntryId(td.getTimesheetEntry().getTimesheetEntryId());
				if (dtlsList == null || dtlsList.isEmpty()) {
					Optional<List<TimesheetEntryApproval>>  approveOpt = timesheetApprovalRepo.getAllTimesheetEntryApprovalByEntryId(td.getTimesheetEntry().getTimesheetEntryId());
					
					if (approveOpt.isPresent()) {
						List<TimesheetEntryApproval> applist = approveOpt.get();
						if (!applist.isEmpty()) {
							TimesheetEntryApproval approvalObj = applist.get(0);
							approvalObj.setIsActive(0);
							timesheetApprovalRepo.saveAndFlush(approvalObj);
							timesheetEntryRepo.findEntryById(td.getTimesheetEntry().getTimesheetEntryId()).ifPresent((te)->{
								te.setIsActive(0);
								timesheetEntryRepo.saveAndFlush(te);
							});
						}
					}
				}
			}
			log.info(method + "Exit");
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
	}
}
