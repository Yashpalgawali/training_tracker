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

import com.example.demo.dto.DepartmentDto;

import jakarta.servlet.http.HttpServletResponse;

public class ExportAllDepartments {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<DepartmentDto> DeptList;

	public ExportAllDepartments(List<DepartmentDto> dlist) {
		this.DeptList = dlist;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("All Departments And Company List");
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(12);

		style.setFont(font);

		createCell(row, 0, "Sr No.", style);
		createCell(row, 1, "Department", style);
		createCell(row, 2, "Company", style);

	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		int sr = 1;

		if (DeptList.size() > 0) {
			for (DepartmentDto dept : DeptList) {
				Row row = sheet.createRow(rowCount++);

				int columnCount = 0;

				createCell(row, columnCount++, sr++, style);
				createCell(row, columnCount++, dept.getDept_name(), style);
				createCell(row, columnCount++, dept.getComp_name(), style);

			}
		} else {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, "No Departments Found ", style);
		}
	}

	public byte[] export(HttpServletResponse resp) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		workbook.write(bos);
		workbook.close();
		bos.close();
		return bos.toByteArray();
	}
}
