package com.jhelper.export.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;

import com.jhelper.export.ExcelWriter;

public class SimpleExcelExporter implements Closeable {

	private ExcelWriter excelWriter;
	private String tmpdir;

	private CellStyleSupport headerStyle;
	private CellStyleSupport bodyStyle;

	public SimpleExcelExporter() {
		this.excelWriter = new ExcelWriter();
		this.excelWriter.trackAllColumnsForAutoSizing();

		this.headerStyle = ExcelUtils.getHeaderStyle(excelWriter.getWorkbook());
		this.bodyStyle = ExcelUtils.getDefaultStyle(excelWriter.getWorkbook());
	}

	public void setTmpdir(String tmpdir) {
		this.tmpdir = tmpdir;
	}

	public String getTmpDir() {

		String tmp = this.tmpdir;

		if (tmp == null) {
			tmp = System.getProperty("java.io.tmpdir");
		}

		return tmp;

	}

	public ExcelWriter getExcelWriter() {
		return excelWriter;
	}

	private File createTempFile(String suffix) throws IOException {

		Path tmpDirPath = Paths.get(getTmpDir());

		if (!Files.isDirectory(tmpDirPath)) {
			Files.createDirectories(tmpDirPath);
		}

		String prefix = "";

		return Files.createTempFile(tmpDirPath, prefix, suffix).toFile();
	}

	public File export() throws IOException {

		File savedFile = createTempFile("_xlsx");
		excelWriter.write(savedFile);

		return savedFile;
	}

	public File export(String[] head, Collection<SimpleCell[]> data) throws IOException {

		writeHead(head);
		writeData(data);

		for (int i = 0; i < head.length; i++) {
			excelWriter.autoSizeColumn(i);
		}

		return export();
	}

	public File export(String[] head, DataHandler dataHandler) throws IOException {

		writeHead(head);

		while (true) {
			Collection<Object[]> data = dataHandler.read();

			if (data == null || data.isEmpty()) {
				break;
			}

			writeData(toSimpleCell(data));
		}

		for (int i = 0; i < head.length; i++) {
			excelWriter.autoSizeColumn(i);
		}

		return export();

	}

	public File exportWithMap(String[] head, Collection<Object[]> data) throws IOException {
		return export(head, toSimpleCell(data));
	}

	public Collection<SimpleCell[]> toSimpleCell(Collection<Object[]> data) {
		return data.stream()
				.map(values -> Arrays.stream(toSimpleCell(values)).toArray(SimpleCell[]::new))
				.collect(Collectors.toList());
	}

	public SimpleCell[] toSimpleCell(Object[] values) {
		return Arrays.stream(values).map(value -> new SimpleCell(value)).toArray(SimpleCell[]::new);
	}

	public void setColumnType(Collection<SimpleCell[]> data, int column, SimpleCell.Type type) {
		data.stream().forEach(values -> {
			values[column].setType(type);
		});
	}

	public void writeHead(String[] values) {
		excelWriter.setStyleSupport(headerStyle); // header style
		Arrays.stream(values).forEach(value -> {
			excelWriter.cell().cellValue(value);
			excelWriter.nextCell();
		});

		excelWriter.nextRow();
	}

	public void writeData(Iterable<SimpleCell[]> data) {
		data.forEach(row -> {
			writeData(row);
		});
	}

	public void writeData(SimpleCell[] values) {
		excelWriter.setStyleSupport(bodyStyle); // body style
		Arrays.stream(values).forEach(simpleCell -> {
			setCellValue(excelWriter.cell(), simpleCell);
			excelWriter.nextCell();
		});
		excelWriter.nextRow();
	}

	private void setCellValue(CellSupport cell, SimpleCell simpleCell) {

		Object value = simpleCell.getValue();

		if (SimpleCell.Type.BOOLEAN.equals(simpleCell.getType())) {
			setBoolValue(cell, value);
		} else if (SimpleCell.Type.NUMBER.equals(simpleCell.getType())) {
			setNumberValue(cell, value);
		} else if (SimpleCell.Type.INTEGER.equals(simpleCell.getType())) {
			setIntegerValue(cell, value);
		} else if (SimpleCell.Type.DOUBLE.equals(simpleCell.getType())) {
			setDoubleValue(cell, value);
		} else if (SimpleCell.Type.STRING.equals(simpleCell.getType())) {
			setStringValue(cell, value);
		} else if (SimpleCell.Type.DATE.equals(simpleCell.getType())) {
			setDateValue(cell, value);
		} else if (SimpleCell.Type.DATETIME.equals(simpleCell.getType())) {
			setDateTimeValue(cell, value);
		}
	}

	private void setBoolValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		Boolean b = null;

		if (value instanceof Boolean) {
			b = (Boolean) value;
		} else if (value instanceof String) {
			b = Boolean.parseBoolean(Objects.toString(value));
		}

		cell.cellValue(b);
	}

	private void setNumberValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		Integer i = null;

		if (value instanceof Integer) {
			i = (Integer) value;
		} else if (value instanceof String) {
			i = Integer.parseInt(Objects.toString(value));
		}

		cell.dataFormat("#,#").cellValue(i);
	}

	private void setIntegerValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		Integer i = null;

		if (value instanceof Integer) {
			i = (Integer) value;
		} else if (value instanceof String) {
			i = Integer.parseInt(Objects.toString(value));
		}

		cell.cellValue(i);
	}

	private void setDoubleValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		Double d = null;

		if (value instanceof Double) {
			d = (Double) value;
		} else if (value instanceof String) {
			d = Double.parseDouble(Objects.toString(value));
		}

		cell.cellValue(d);
	}

	private void setStringValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		cell.cellValue(Objects.toString(value));
	}

	private void setDateValue(CellSupport cell, Object value) {

		if (value == null) {
			return;
		}

		try {
			Date date = null;

			if (value instanceof Date) {
				date = (Date) value;
			} else if (value instanceof String) {
				date = DateUtils.parseDate(((String) value).substring(0, 8), "yyyyMMdd");
			}

			cell.dataFormat("yyyy-MM-dd").cellValue(date);
		} catch (ParseException e) {
			cell.cellValue(Objects.toString(value));
		}
	}

	private void setDateTimeValue(CellSupport cell, Object value) {
		if (value == null) {
			return;
		}

		try {
			Date date = null;

			if (value instanceof Date) {
				date = (Date) value;
			} else if (value instanceof String) {
				date = DateUtils.parseDate((String) value, "yyyyMMddHHmmss");
			}

			cell.dataFormat("yyyy-MM=dd HH:mm:ss").cellValue(date);
		} catch (ParseException e) {
			cell.cellValue(Objects.toString(value));
		}
	}

	@Override
	public void close() throws IOException {
		excelWriter.close();
	}
}