package com.jhelper.export.excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.jhelper.export.ExcelReader;

@Component
public class SimpleExcelReader {

	private String tmpdir;

	public void setTmpdir(String tmpdir) {
		this.tmpdir = tmpdir;
	}

	public List<Object[]> read(File file) throws IOException {

		File tmpFile = null;

		try {
			tmpFile = Files.createTempFile(Paths.get(tmpdir), "", "_xlsx").toFile();

			return readFromFile(tmpFile);
		} finally {
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
	}

	private List<Object[]> readFromFile(File file) throws IOException {
		ExcelReader reader = new ExcelReader();
		return reader.read(file);
	}
}
