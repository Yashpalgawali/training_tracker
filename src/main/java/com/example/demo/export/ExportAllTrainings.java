package com.example.demo.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.demo.dto.EmployeeTrainingDto;

import jakarta.servlet.http.HttpServletResponse;

public class ExportAllTrainings {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<EmployeeTrainingDto> EmployeeTrainingHistory;
	
	public ExportAllTrainings(List<EmployeeTrainingDto> alist) {
		this.EmployeeTrainingHistory = alist;
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine()
	{
		sheet = workbook.createSheet("AllEmployeeTrainingHistory");
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);
		
		style.setFont(font);
	
		createCell(row,0,"Sr No.",style);
		createCell(row,1,"Employee",style);
		createCell(row,2,"Training",style);
		createCell(row,3,"Training Date",style);
		createCell(row,4,"Completion Date",style);
		createCell(row,5,"Designation",style);
		createCell(row,6,"Department",style);
		createCell(row,7,"Company",style);
		createCell(row,8,"Competency",style);
		 
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
		int sr=1;
		
		if(EmployeeTrainingHistory.size() > 0 ) {
		for(EmployeeTrainingDto training : EmployeeTrainingHistory)
		{
			Row row = sheet.createRow(rowCount++);
			
			int columnCount = 0;
			
			createCell(row,columnCount++, sr++ ,style);
			createCell(row,columnCount++, training.getEmp_name() ,style);
			createCell(row,columnCount++, training.getTraining_name() ,style);
			createCell(row,columnCount++, training.getTraining_date() ,style);
			if(training.getCompletion_date()!=null) {
				if(!training.getCompletion_date().equals("")) {
					createCell(row,columnCount++, training.getCompletion_date(), style);
				}
				else {
					createCell(row,columnCount++, "Not Completed", style);
				}
			}
			else {
				createCell(row,columnCount++, "Not Completed", style);
			}
			createCell(row,columnCount++, training.getDesig_name() ,style);
			createCell(row,columnCount++, training.getDept_name() ,style);
			createCell(row,columnCount++, training.getComp_name() ,style);
			createCell(row,columnCount++, training.getScore() ,style);
		  }
	    }
		else {
			Row row = sheet.createRow(rowCount++);
			
			int columnCount = 0;
			
			createCell(row,columnCount++, "No Data Found " ,style);
		}
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
