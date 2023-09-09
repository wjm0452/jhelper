package com.jhelper.export.excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {

    static public CellStyleSupport getHeaderStyle(Workbook workbook) {
        return CellStyleSupport.builder(workbook)
                .fontName("맑은 고딕")
                .fontSize(11)
                .align(HorizontalAlignment.CENTER)
                .borderStyle(BorderStyle.THIN)
                .foregroundColor(IndexedColors.GREY_40_PERCENT);
    }

    static public CellStyleSupport getDefaultStyle(Workbook workbook) {
        return CellStyleSupport.builder(workbook)
                .fontName("맑은 고딕")
                .fontSize(11)
                .borderStyle(BorderStyle.THIN)
                .wrapText(true);
    }

}
