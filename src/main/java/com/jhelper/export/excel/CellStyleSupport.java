package com.jhelper.export.excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyleSupport {
	
	private Workbook workbook;
	private CellStyle style;
	private Font font;
	
	public static CellStyleSupport builder (Workbook workbook) {
		return new CellStyleSupport(workbook);
	}
	
	public static CellStyleSupport builder (Workbook workbook, CellStyle style) {
		return new CellStyleSupport(workbook, style);
	}
	
	public CellStyleSupport(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public CellStyleSupport(Workbook workbook,  CellStyle style) {
		this.workbook = workbook;
		this.style = style;
	}
	
	private Font getFont() {
		if (font == null) {
			int fontIndex = getStyle().getFontIndexAsInt();
			if(fontIndex > 0 ) {
				font = workbook.getFontAt(fontIndex);
			} else {
				font = workbook.createFont();
				getStyle().setFont(font);
			}
			
		}
		
		return font;
	}
	
	public CellStyle getStyle() {
		if(style == null) {
			style = workbook.createCellStyle();
		}
		
		return style;
	}
	
	public CellStyleSupport style(CellStyle style) {
		this.style = style;
		return this;
	}
	
	public CellStyleSupport fontName(String fontName) {
		getFont().setFontName(fontName);
		return this;
		
	}
	
	public CellStyleSupport fontSize(int fontSize) {
		getFont().setFontHeightInPoints((short) fontSize);
		return this;
	}
	
	public CellStyleSupport fontColor(IndexedColors color) {
		getFont().setColor(color.getIndex());
		return this;
	}
	
	public CellStyleSupport foregroundColor(IndexedColors color) {
		foregroundColor(FillPatternType.SOLID_FOREGROUND, color);
		return this;
	}
	
	public CellStyleSupport foregroundColor(FillPatternType patternType, IndexedColors color) {
		getStyle().setFillForegroundColor(color.getIndex());
		getStyle().setFillPattern(patternType);
		return this;
	}
	
	public CellStyleSupport align(HorizontalAlignment align) {
		getStyle().setAlignment(align);
		return this;
	}
	
	public CellStyleSupport verticalAlign(VerticalAlignment align) {
		getStyle().setVerticalAlignment(align);
		return this;
	} 
	
	public CellStyleSupport borderColor(IndexedColors color) {
		borderColor(CellDirection.TOP, color);
		borderColor(CellDirection.RIGHT, color);
		borderColor(CellDirection.BOTTOM, color);
		borderColor(CellDirection.LEFT, color);
		return this;
	} 
	
	public CellStyleSupport borderColor(CellDirection direction, IndexedColors color) {
		if(direction == CellDirection.TOP) {
			getStyle().setTopBorderColor(color.getIndex());
		} else if(direction == CellDirection.RIGHT) {
			getStyle().setRightBorderColor(color.getIndex());
		} else if(direction == CellDirection.BOTTOM) {
			getStyle().setBottomBorderColor(color.getIndex());
		} else if(direction == CellDirection.LEFT) {
			getStyle().setLeftBorderColor(color.getIndex());
		}
	
		return this;
	} 
	
	public CellStyleSupport borderStyle(BorderStyle borderStyle) {
		borderStyle(CellDirection.TOP, borderStyle);
		borderStyle(CellDirection.RIGHT, borderStyle);
		borderStyle(CellDirection.BOTTOM, borderStyle);
		borderStyle(CellDirection.LEFT, borderStyle);
		return this;
	}
	
	public CellStyleSupport borderStyle(CellDirection direction, BorderStyle borderStyle) {
		if(direction == CellDirection.TOP) {
			getStyle().setBorderTop(borderStyle);
		} else if(direction == CellDirection.RIGHT) {
			getStyle().setBorderRight(borderStyle);
		} else if(direction == CellDirection.BOTTOM) {
			getStyle().setBorderBottom(borderStyle);
		} else if(direction == CellDirection.LEFT) {
			getStyle().setBorderLeft(borderStyle);
		}
	
		return this;
	} 
	
	public CellStyleSupport dataFormat(String format) {
		DataFormat dataFormat = workbook.createDataFormat();
		getStyle().setDataFormat(dataFormat.getFormat(format));
		
		return this;
	}
	
	public CellStyleSupport wrapText(boolean wrapped) {
		getStyle().setWrapText(wrapped);
		return this;
	}
	
}
