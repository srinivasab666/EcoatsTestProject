package com.ecoat.management.ecoatapi.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.ReportDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.ClientAddress;
import com.ecoat.management.ecoatapi.model.CorporateAddress;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeBilling;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.response.TimesheetReportResponse;
import com.ecoat.management.ecoatapi.repository.CorporateAddressRepository;
import com.ecoat.management.ecoatapi.util.CommonUtil;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class InvoiceGenerator {

	@Autowired EmployeeService employeeService;
	@Autowired
	CorporateAddressRepository corporateAddressRepository;
	@Autowired
	ClientService clientService;
	@Autowired
	EmployeeBillingService employeeBillingService;
	public void generateInvoice(double hours, ReportDTO reportDTO) throws IOException {

		try{


			double totalBilledRate = 0.0;
			Employee employee = employeeService.getEmployeeById(reportDTO.getEmployeeId());
			java.util.List<EmployeeBilling> employeeBillings = employeeBillingService.getAllEmployeeBilling(reportDTO.getEmployeeId());
			EmployeeBilling employeeBilling1 = new EmployeeBilling();
			if(employeeBillings.size()>0){
				employeeBilling1 = employeeBillings.get(0);
			}
			totalBilledRate = employeeBilling1.getBillingRate()*hours;
			CorporateAddress corporateAddress = new CorporateAddress();
			if(null != employee && null != employee.getCorporate()){
				Optional<CorporateAddress> corpAddr = corporateAddressRepository.findById(employee.getCorporate().getId());
				if(corpAddr.isPresent()){
					corporateAddress = corpAddr.get();
				}
			}
			java.util.List<Client> clients = clientService.getClientsByCorp(employee.getCorporate().getId());
			Client client = clients.get(0);
			Set<ClientAddress> clientAddresses = client.getClientAddress();
			ClientAddress clientAddress = clientAddresses.iterator().next();

			PdfWriter writer = new PdfWriter(getPdfNameWithDate());
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Random random = new Random();
			int x = random.nextInt(900) + 100;

			String invoiceNumber = employee.getCorporate().getCorporateCode().substring(0,2)+employee.getEmpId()
			+LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"))+x;

			LocalDate ld = LocalDate.now();

			float col = 280f;
			float columnWidth[] = { col, col };
			Table table = new Table(columnWidth);

			table.addCell(new Cell().add(new Paragraph(employee.getCorporate().getCorporateName()+"\n"
					+corporateAddress.getAddress1()+"\n"
					+corporateAddress.getCity()+"\n"
					+corporateAddress.getState()+"\n"
					+corporateAddress.getCountry()+"\n"
					+corporateAddress.getZip()+"\n"
					+corporateAddress.getPhone()+"\n")
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setMarginTop(5f).setMarginBottom(50f).setFontSize(15f)));

			table.addCell(new Cell().add(new Paragraph("Invoice Number#: " + invoiceNumber + "\n" + "Date: " + ld)
					.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP).setMarginTop(5f)
					.setMarginBottom(50f)));

			Table corpDetails = new Table(columnWidth);
			corpDetails.addCell(new Cell(0, 2).add(new Paragraph("ISSUED TO:").setBold().setFontSize(15f)));
			corpDetails.addCell(new Cell(0, 2).add(new Paragraph(clients.get(0).getClientName()).setBold())
					.add(new Paragraph().add(clientAddress.getClientAddress1()+"\n"
					+clientAddress.getCity()+"\n"
					+clientAddress.getState()+"\n"
					+clientAddress.getCountry()+"\n"
					+clientAddress.getZip()+"\n"
					+clientAddress.getPhone()).setMarginBottom(60f)));

			float invoiceColWidth[] = { 190, 110, 90, 60, 110 };
			Table invoiceTable = new Table(invoiceColWidth);
			invoiceTable.addCell(new Cell().add(new Paragraph("Description")));
			invoiceTable.addCell(new Cell().add(new Paragraph("Week ending")));
			invoiceTable.addCell(new Cell().add(new Paragraph("Hours")));
			invoiceTable.addCell(new Cell().add(new Paragraph("Rate")));
			invoiceTable.addCell(new Cell().add(new Paragraph("Amount")));

			invoiceTable.addCell(
					new Cell().add(new Paragraph("Work performed by "+employee.getFirstName()+" "+
							employee.getLastName()+" for the period "+reportDTO.getFromDate()+" to "+reportDTO.getToDate())
							.setMarginBottom(40f)));
			invoiceTable.addCell(new Cell().add(new Paragraph(reportDTO.getToDate()+"").setMarginBottom(40f)));
			invoiceTable.addCell(new Cell().add(new Paragraph(hours+"hours").setMarginBottom(40f)));
			invoiceTable.addCell(new Cell().add(new Paragraph("$"+employeeBilling1.getBillingRate()).setMarginBottom(40f)));
			invoiceTable.addCell(new Cell().add(new Paragraph("$"+totalBilledRate).setMarginBottom(40f)));

			document.add(table);
			document.add(corpDetails);
			document.add(invoiceTable);
//            document.open();
//            addLogo(document);
//            addDocTitle(document);
//            createTable(document,2);
//            addFooter(document);
			document.close();
			System.out.println("------------------Your PDF Report is ready!-------------------------");

		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public ByteArrayInputStream prepareStatusReportPDF(java.util.List<TimesheetEntry> entries,AppraisalCommentsDTO appraisalDto){
		String method = "InvoiceGenerator.prepareStatusReportPDF ";
		log.info(method + "Enter");
		String managerName = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Map<String,String> leaves = new HashMap<>();
		try {
			Employee employee = employeeService.getEmployeeById(appraisalDto.getEmpID());
			if (employee.getManager() != null) {
				managerName = employee.getManager().getFirstName() +" "+employee.getManager().getLastName();
			}
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			float columnWidth[] = { 100f, 160f,100f,200f };
			Table empDtlsTbl = new Table(columnWidth);
			empDtlsTbl.addCell(new Cell(0,4).add(new Paragraph("Employee Status Report").setBold().setUnderline().setFontSize(20f)
					.setTextAlignment(TextAlignment.CENTER).setMarginBottom(15f)).setBorder(Border.NO_BORDER));
			
			empDtlsTbl.addCell(new Cell().add(new Paragraph("Employee Name ").setBold().setFontSize(12f).setTextAlignment(TextAlignment.RIGHT).setMargin(5f)));
			empDtlsTbl.addCell(new Cell().add(new Paragraph(employee.getFirstName()+" "+employee.getLastName())
					.setFontSize(12f).setTextAlignment(TextAlignment.LEFT).setMargin(5f)));
			empDtlsTbl.addCell(new Cell().add(new Paragraph("Supervisor ").setBold().setFontSize(12f).setTextAlignment(TextAlignment.RIGHT).setMargin(5f)));
			empDtlsTbl.addCell(new Cell().add(new Paragraph(managerName).setFontSize(12f).setTextAlignment(TextAlignment.LEFT).setMargin(5f)));
			
			empDtlsTbl.addCell(new Cell().add(new Paragraph("Status Period ").setBold().setFontSize(12f).setTextAlignment(TextAlignment.RIGHT).setMargin(5f)));
			empDtlsTbl.addCell(new Cell().add(new Paragraph(appraisalDto.getFromDate()+" to "+appraisalDto.getToDate())
					.setFontSize(12f).setTextAlignment(TextAlignment.LEFT).setMargin(5f)));
			document.add(empDtlsTbl);
			List l  = new List(ListNumberingType.DECIMAL);
			entries.stream().forEach(te ->{
				String comments = te.getAppraisalComments();
				Set<String> projects = new HashSet<>();
				te.getTimesheetEntryDetails().stream().forEach(ted -> {
					if(ted.getTimeSheetLeaveTypes() !=null) {
						leaves.put(CommonUtil.convertToLocalDate(ted.getEntryDate()).toString(), ted.getTimeSheetLeaveTypes().getLeaveName().getLeave());
					}
					if (ted.getProject()!=null) {
						projects.add(ted.getProject().getProjectName());
					}
				});
				Paragraph projPara = null; 
				StringBuffer workDtls = new StringBuffer();
				if (!projects.isEmpty()) {
					 projPara = new Paragraph("Projects: ").setBold().add(projects.toString()+"\n");
					 workDtls.append("Projects: "+String.join(", ", projects)+"\n");
				}
				if (StringUtils.hasText(comments)) {
					projPara.add("Work Completed Deliverables for the Period: "+ 
				CommonUtil.convertToLocalDate(te.getFromDate()) +" to "+ CommonUtil.convertToLocalDate(te.getToDate())+"\n");
					workDtls.append("Work Completed Deliverables for the Period: "+ 
				CommonUtil.convertToLocalDate(te.getFromDate()) +" to "+ CommonUtil.convertToLocalDate(te.getToDate())+"\n");
					projPara.add(comments+"\n");
					workDtls.append(comments+"\n");
					l.add((ListItem) new ListItem(workDtls.toString()).setBold().setMarginTop(10f));
				}
			});
					document.add(l);
			Paragraph timeOffHeading = new Paragraph("Scheduling Time Off").setBold().setFontSize(20f).setTextAlignment(TextAlignment.LEFT);
			float leavesColumnWidth[] = { 100f,180f };
			Table leavesTbl = new Table(leavesColumnWidth);
			leavesTbl.addCell(new Cell().add(new Paragraph("Date Affected").setFontSize(10f).setTextAlignment(TextAlignment.LEFT)));
			leavesTbl.addCell(new Cell().add(new Paragraph("Reason").setFontSize(10f).setTextAlignment(TextAlignment.LEFT)));
			
			leaves.forEach((k,v)->{
				leavesTbl.addCell(new Cell().add(new Paragraph(k).setFontSize(10f).setTextAlignment(TextAlignment.LEFT)));
				leavesTbl.addCell(new Cell().add(new Paragraph(v).setFontSize(10f).setTextAlignment(TextAlignment.LEFT)));
			});
//			Paragraph p = new Paragraph("Work Completed/ Deliverables This Period").setFontSize(20f).setTextAlignment(TextAlignment.LEFT);
//			Cell listCell = new Cell();
//			List l  = new List();
//			entries.forEach(c ->{
//				l.add(new ListItem(c));
//			});
			document.add(timeOffHeading);
			document.add(leavesTbl);
			document.close();
			log.info(method + "Exit");
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw new EcoatsException(e.getMessage());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	void populateInvoiceBean(java.util.List<TimesheetReportResponse> customReport, ReportDTO reportDTO){

	}

	private static void leaveEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private String getPdfNameWithDate() {
		String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MMMM_yyyy"));
		return "D:/PdfReportRepo/" + "reportFileName" + "-" + localDateString + ".pdf";
	}
	
//    private void addLogo(Document document) {
//        try {
//            Image img = Image.getInstance("D:/Bhuvi/bhuvi/64.jpg");
//            //img.scalePercent(50, 50);
//            img.scaleToFit(100,20);
//            img.setAlignment(Element.ALIGN_LEFT);
//            document.add(img);
//        } catch (DocumentException | IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private void addDocTitle(Document document) throws DocumentException {
//        String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss"));
//        Paragraph p1 = new Paragraph();
//        leaveEmptyLine(p1, 1);
//        p1.add(new Paragraph("D:/PdfReportRepo/", COURIER));
//        p1.setAlignment(Element.ALIGN_CENTER);
//        leaveEmptyLine(p1, 1);
//        p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));
//
//        document.add(p1);
//
//    }
//
//    private void createTable(Document document, int noOfColumns) throws DocumentException {
//        Paragraph paragraph = new Paragraph();
//        leaveEmptyLine(paragraph, 3);
//        document.add(paragraph);
//
//        PdfPTable table = new PdfPTable(noOfColumns);
//
//        for(int i=0; i<noOfColumns; i++) {
//            PdfPCell cell = new PdfPCell(new Phrase("COloumn1"));
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            cell.setBackgroundColor(BaseColor.CYAN);
//            table.addCell(cell);
//        }
//
//        table.setHeaderRows(1);
//        getDbData(table);
//        document.add(table);
//    }
//
//    private void getDbData(PdfPTable table) {
//
//        //List<Employee> list = eRepo.getAllEmployeeData();
//        for (int i=0;i<2;i++) {
//
//            table.setWidthPercentage(100);
//            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//            table.addCell("123");
//            table.addCell("ram");
////            table.addCell(employee.getEmpDept());
////            table.addCell(currencySymbol + employee.getEmpSal().toString());
//
////            System.out.println(employee.getEmpName());
//        }
//
//    }
//
//    private void addFooter(Document document) throws DocumentException {
//        Paragraph p2 = new Paragraph();
//        leaveEmptyLine(p2, 1);
//        p2.setAlignment(Element.ALIGN_MIDDLE);
//        p2.add(new Paragraph(
//                "------------------------End Of " +"Employee-Report"+"------------------------",
//                COURIER_SMALL_FOOTER));
//
//        document.add(p2);
//    }

}
