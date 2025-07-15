package com.example.demo.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

//import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.entity.EmployeeTrainingHistory;

import jakarta.servlet.http.HttpServletResponse;

public class ExportEmployeeTrainingHistory {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private List<EmployeeTrainingHistory> ahist;
	
	public ExportEmployeeTrainingHistory(List<EmployeeTrainingHistory> ahist)
	{
		this.ahist = ahist;
		workbook = new XSSFWorkbook();
	}
	
	public void writeHeaderLine() {
			
	    String sheetName = "Employee Training History";
	    
	    // Check if sheet with the same name already exists
	    Sheet existingSheet = workbook.getSheet(sheetName);

	    if (existingSheet != null) {
	        sheet = (XSSFSheet) existingSheet;
	        System.out.println("Sheet already exists == "+sheetName);
	    } else {
	        sheet = workbook.createSheet(sheetName);
	    }
			
			Row row = sheet.createRow(0); 
			
			CellStyle style = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontHeight(12);
			
			style.setFont(font);
		
			createCell(row,0,"Sr No.",style);

			createCell(row,1,"Training",style);
			createCell(row,2,"Training Date",style);
			createCell(row,3,"Completion Date",style);
			createCell(row,4,"Employee",style);
	
	}
	
	public void createCell(Row row,int columnCount, Object value,CellStyle style)
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
	
	public void writeDataLines() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		 
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		int sr=1,cn=1;		
		if(ahist.size() > 0) {
      	for(EmployeeTrainingHistory hist : ahist)
			{
				Row row = sheet.createRow(rowCount++);				
				int columnCount = 0;
				
				createCell(row,columnCount++, sr++ ,style);
				createCell(row,columnCount++, hist.getTraining().getTraining_name() ,style);			
				createCell(row,columnCount++, hist.getTraining_date() ,style);
			
				if(!hist.getCompletion_date().equals("")) {				 
					createCell(row,columnCount++, hist.getCompletion_date() ,style);
				}
				else {
					createCell(row,columnCount++, "Not Completed" , style );
				}
	
				if(cn==1) {
					createCell(row,columnCount++, hist.getEmployee().getEmp_name() ,style);
					cn++;
				}
			}
		}
		else {
			Row row = sheet.createRow(rowCount++);			
			int columnCount = 0;			
			createCell(row,columnCount++, "No Data Found of the Employee" ,style);
		}
	}
	
	public byte[] export(HttpServletResponse response)throws IOException {
		writeHeaderLine();
		writeDataLines();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		return outputStream.toByteArray();
	}
	
}
