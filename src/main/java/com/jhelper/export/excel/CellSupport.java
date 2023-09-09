package com.jhelper.export.excel;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class CellSupport {
	private Cell cell;
	private CellStyleSupport styleSupport;
	
	public static CellSupport builder (Cell cell) {
		return new CellSupport(cell);
		
	}
	
	public CellSupport(Cell cell) {
		this.cell = cell;
	}

	public CellStyleSupport style() {
		if(styleSupport == null) {
			Workbook workbook = cell.getSheet().getWorkbook();
			CellStyle style = workbook.createCellStyle();
			styleSupport = CellStyleSupport.builder(workbook, style);
			cell.setCellStyle(style);
			
		}
		
		return styleSupport;
	}
	
	public CellSupport style(CellStyleSupport styleSupport) {
		this.styleSupport = styleSupport;
		cell.setCellStyle(styleSupport.getStyle());
		return this;
	}
	
	public CellSupport dataFormat(String format) {
		style().dataFormat(format);
		return this;
		
	}
	
	public CellSupport cellValue(boolean value) {
		cell.setCellValue(value);
		return this;
		
	}
	
	public CellSupport cellValue(int value) {
		cell.setCellValue(value);
		this.dataFormat("#");
		return this;
	}
	
	public CellSupport cellValue(double value) {
		cell.setCellValue(value);
		return this;
	}
	
	public CellSupport cellValue(String value) {
		if(value != null) {
			cell.setCellValue(value);
		}
		
		return this;
	}
	
	public CellSupport cellValue(Date value) {
		if(value != null) {
			cell.setCellValue(value);
		}
		
		return this;
	}
	
}
