package com.jhelper.export;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import com.jhelper.export.excel.CellStyleSupport;
import com.jhelper.export.excel.CellSupport;

@Component
public class ExcelWriter implements Closeable {

    private final static int ROW_ACCESS_WINDOW_SIZE = 1000;

    private Workbook workbook;
    private Sheet sheet;

    private CellStyleSupport styleSupport;

    public ExcelWriter() {
        this(new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE));
    }

    public ExcelWriter(Workbook workbook) {
        this.workbook = workbook;
        newSheet();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void newSheet() {
        sheet = workbook.createSheet();
        setActiveCell(0, 0);
    }

    public void setSheet(String name) {
        sheet = workbook.getSheet(name);
    }

    public void setSheet(int sheetIndex) {
        sheet = workbook.getSheetAt(sheetIndex);
    }

    public void setSheetName(String sheetName) {
        int sheetIndex = workbook.getSheetIndex(sheet);
        workbook.setSheetName(sheetIndex, sheetName);
    }

    public int getRowIndex() {
        CellAddress address = sheet.getActiveCell();

        if (address == null) {
            return -1;
        }

        return address.getRow();
    }

    public int getColumnIndex() {

        CellAddress address = sheet.getActiveCell();

        if (address == null) {
            return -1;
        }

        return address.getColumn();
    }

    public Row getRow() {
        return getRow(getRowIndex());
    }

    public Cell getCell() {
        return getCell(getRowIndex(), getColumnIndex());
    }

    public Row getRow(int rowIndex) {

        if (rowIndex < 0) {
            return null;
        }

        return sheet.getRow(rowIndex);
    }

    public Cell getCell(int rowIndex, int cellIndex) {

        Row row = getRow(rowIndex);

        if (cellIndex < 0) {
            return null;
        }

        return row != null ? row.getCell(cellIndex) : null;
    }

    public void setActiveCell(int rowIndex, int cellIndex) {

        Cell cell = getCell(rowIndex, cellIndex);
        CellAddress address = null;

        if (cell != null) {
            address = cell.getAddress();
        } else {
            address = new CellAddress(rowIndex, cellIndex);
        }

        sheet.setActiveCell(address);
    }

    public Row createRow(int rowIndex) {

        Row row = getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        return row;
    }

    public Cell createCell(int rowIndex, int cellIndex) {

        Row row = createRow(rowIndex);
        Cell cell = row.getCell(cellIndex);

        if (cell == null) {
            cell = row.createCell(cellIndex);
        }

        return cell;
    }

    public Cell createCell() {
        return createCell(getRowIndex(), getColumnIndex());
    }

    public ExcelWriter nextRow() {
        setActiveCell(getRowIndex() + 1, 0);
        return this;
    }

    public ExcelWriter nextCell() {
        setActiveCell(getRowIndex(), getColumnIndex() + 1);
        return this;
    }

    public void setRowHeight(int height) {
        setRowHeight(getRowIndex(), height);
    }

    public void setRowHeight(int rowIndex, int height) {
        createRow(rowIndex).setHeightInPoints(height);
    }

    public void setColumnWidth(int width) {
        setColumnWidth(getColumnIndex(), width);
    }

    public void setColumnWidth(int columnIndex, int width) {
        sheet.setColumnWidth(columnIndex, ((width * 7 + 5) / 7 * 256));
    }

    public void trackAllColumnsForAutoSizing() {
        if (sheet instanceof SXSSFSheet) {
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        }
    }

    public void autoSizeColumn(int columnIndex) {
        sheet.autoSizeColumn(columnIndex);
    }

    public void setStyleSupport(CellStyleSupport styleSupport) {
        this.styleSupport = styleSupport;
    }

    public CellSupport cell() {
        return new CellSupport(createCell()).style(styleSupport);
    }

    public CellSupport mergeCell(final int addRowNumber, final int addCellNumber) {

        int rowIndex = getRowIndex();
        int cellIndex = getColumnIndex();

        CellSupport firstCell = new CellSupport(createCell()).style(styleSupport);

        CellRangeAddress region = new CellRangeAddress(rowIndex,
                rowIndex + addRowNumber,
                cellIndex,
                cellIndex + addCellNumber);

        CellStyle style = styleSupport.getStyle();

        RegionUtil.setBorderTop(style.getBorderTop(), region, sheet);
        RegionUtil.setBorderRight(style.getBorderTop(), region, sheet);
        RegionUtil.setBorderBottom(style.getBorderTop(), region, sheet);
        RegionUtil.setBorderLeft(style.getBorderTop(), region, sheet);

        RegionUtil.setTopBorderColor(style.getTopBorderColor(), region, sheet);
        RegionUtil.setRightBorderColor(style.getRightBorderColor(), region, sheet);
        RegionUtil.setBottomBorderColor(style.getBottomBorderColor(), region, sheet);
        RegionUtil.setLeftBorderColor(style.getLeftBorderColor(), region, sheet);

        sheet.addMergedRegion(region);

        setActiveCell(rowIndex, cellIndex + addCellNumber);

        return firstCell;
    }

    public void write(File file) throws IOException {
        write(file.toPath());
    }

    public void write(Path path) throws IOException {
        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            write(os);
        }
    }

    public void write(OutputStream os) throws IOException {
        workbook.write(os);
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}
