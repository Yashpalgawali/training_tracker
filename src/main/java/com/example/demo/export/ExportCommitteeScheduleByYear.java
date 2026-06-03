package com.example.demo.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.CommitteeSchedule;
import com.example.demo.service.ICommitteeScheduleService;

import jakarta.servlet.http.HttpServletResponse;

public class ExportCommitteeScheduleByYear {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<CommitteeSchedule> committeSchedule;
	
	@Autowired
	private ICommitteeScheduleService committeeScheduleServ;
	
	public ExportCommitteeScheduleByYear(List<CommitteeSchedule> alist) {
		this.committeSchedule = alist;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine()
	{
		sheet = workbook.createSheet("All Committee Schedule for Year");
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		
		style.setFont(font);
	
		createCell(row,0,"Sr No.",style);
		createCell(row,1,"Committee Name",style);
		createCell(row,2,"Frequency",style);
		createCell(row,3,"January",style);
		createCell(row,4,"February",style);
		createCell(row,5,"March",style);
		createCell(row,6,"April",style);
		createCell(row,7,"May",style);
		createCell(row,8,"June",style);
		createCell(row,9,"July",style);
		createCell(row,10,"August",style);
		createCell(row,11,"September",style);
		createCell(row,12,"October",style);
		createCell(row,13,"November",style);
		createCell(row,14,"December",style);
		createCell(row,15,"Done",style);
		createCell(row,16,"Plan",style);
		createCell(row,17,"Done By",style);
		createCell(row,18,"Checked By",style);
		createCell(row,19,"Approved By",style);		
		 
	}
	
	private void createCell(Row row,int columnCount,Object value,CellStyle style)
	{
		  sheet.autoSizeColumn(columnCount);
	        Cell cell = row.createCell(columnCount);
	        if (value instanceof Integer) {
	            cell.setCellValue((Integer) value);
	        } else if (value instanceof Boolean) {
	            cell.setCellValue((Boolean) value);
	        }
	        else if (value instanceof Long) {
	            cell.setCellValue((Long) value);
	        }
	        else {
	            cell.setCellValue((String) value);
	        }
	        cell.setCellStyle(style);
	}
	private void writeDataLines() {
	    int rowCount = 1;
	    CellStyle style = workbook.createCellStyle();
	    XSSFFont font = workbook.createFont();
	    font.setFontHeight(11);
	    style.setFont(font);
	    int sr = 1;

	    if (!committeSchedule.isEmpty()) {

	        // ── Group all month-records by committee (preserves insertion order) ──
	        Map<Long, List<CommitteeSchedule>> byCommittee = committeSchedule.stream()
	            .collect(Collectors.groupingBy(
	                s -> s.getCommittee().getCommitteeId(),
	                LinkedHashMap::new,
	                Collectors.toList()
	            ));

	        for (Map.Entry<Long, List<CommitteeSchedule>> entry : byCommittee.entrySet()) {

	            List<CommitteeSchedule> schedules = entry.getValue();
	            CommitteeSchedule first = schedules.get(0); // for name, frequency, signatures

	            Row row = sheet.createRow(rowCount++);

	            // ── Fixed columns 0-2 ────────────────────────────────────────────
	            createCell(row, 0, sr++,                                      style);
	            createCell(row, 1, first.getCommittee().getCommitteeName(),   style);
	            createCell(row, 2, first.getFrequency().getFrequency(),       style);

	            // ── Build month→date map using FIXED column indices (3–14) ──────
	            //    Each record has exactly one non-blank monthX field.
	            //    We read which month it is and place the date in the right column.
	            String[] monthDates = new String[12]; // index 0=Jan, 11=Dec

	            for (CommitteeSchedule sch : schedules) {
	                if (notBlank(sch.getMonthJan()))  monthDates[0]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthFeb()))  monthDates[1]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthMar()))  monthDates[2]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthApr()))  monthDates[3]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthMay()))  monthDates[4]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthJun()))  monthDates[5]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthJul()))  monthDates[6]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthAug()))  monthDates[7]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthSep()))  monthDates[8]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthOct()))  monthDates[9]  = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthNov()))  monthDates[10] = sch.getCommitteeScheduleDate();
	                if (notBlank(sch.getMonthDec()))  monthDates[11] = sch.getCommitteeScheduleDate();
	            }

	            // ── Write Jan–Dec into columns 3–14 ─────────────────────────────
	            for (int i = 0; i < 12; i++) {
	                createCell(row, 3 + i, safeStr(monthDates[i]), style);
	            }

	            // ── Signatures from first record (columns 15–17) ─────────────────
	            createCell(row, 15, safeStr(first.getDone()),    style);
	            createCell(row, 16, safeStr(first.getPlan()),    style);
	            createCell(row, 17, safeStr(first.getDoneBy()),    style);
	            createCell(row, 18, safeStr(first.getCheckedBy()), style);
	            createCell(row, 19, safeStr(first.getApprovedBy()), style);
	        }

	    } else {
	        Row row = sheet.createRow(rowCount);
	        createCell(row, 0, "No Data Found", style);
	    }
	}

	// ── null-safe helpers ────────────────────────────────────────────────────────
	private boolean notBlank(String value) {
	    return value != null && !value.isBlank();  // fixes != " " and || null bugs
	}

	private String safeStr(String value) {
	    return notBlank(value) ? value : "";
	}

	
	public byte[] export(HttpServletResponse resp)throws IOException
	{
		writeHeaderLine();
		writeDataLines();
		
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}
	
	
}
