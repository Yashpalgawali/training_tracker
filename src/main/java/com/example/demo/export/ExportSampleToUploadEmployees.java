package com.example.demo.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;

public class ExportSampleToUploadEmployees {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	 
	
	public ExportSampleToUploadEmployees() {
	
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine()
	{
		sheet = workbook.createSheet("Sample Employee List");
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		
		style.setFont(font);
	
		createCell(row,0,"Sr No.",style);
		createCell(row,1,"Employee",style);
		createCell(row,2,"Employee Code",style);
		createCell(row,3,"Designation",style);
		createCell(row,4,"Department",style);
		createCell(row,5,"Company",style);
		createCell(row,6,"Joining Date",style);
		createCell(row,7,"Contractor",style);
		createCell(row,8,"Category",style);
		 
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

	private void writeDataLines()
	{
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		
		int columnCount = 8;
		for(int i=0;i<columnCount;i++) {
			Row row = sheet.createRow(rowCount++);
			 
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
			createCell(row,i++, "" ,style);
		}
		
		
		
//		if(EmployeeList.size() > 0 ) {
//		for(EmployeeDTO employee : EmployeeList)
//		{
//			Row row = sheet.createRow(rowCount++);
//			
//			int columnCount = 0;
//			
//			createCell(row,columnCount++, sr++ ,style);
//			createCell(row,columnCount++, employee.getEmp_name() , style);
//			createCell(row,columnCount++, employee.getEmp_code() , style);
//			createCell(row,columnCount++, employee.getDesignation() , style);
//			createCell(row,columnCount++, employee.getDepartment() , style);
//			createCell(row,columnCount++, employee.getCompany() , style);
//			createCell(row,columnCount++, employee.getJoining_date(), style);
//			createCell(row,columnCount++, employee.getContractor_name() ,style);
//			createCell(row,columnCount++, employee.getCategory() ,style);
//		  }
//	    }
//		else {
//			Row row = sheet.createRow(rowCount++);			
//			int columnCount = 0;			
//			createCell(row,columnCount++, "No Data Found " ,style);
//		}
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
