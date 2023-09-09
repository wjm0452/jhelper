package com.jhelper.jserve.web;

import com.jhelper.jserve.web.sql.SqlHelperService;
import com.jhelper.jserve.web.sql.export.SqlExcelExportService;
import com.jhelper.jserve.web.sql.export.SqlJsonExportService;
import com.jhelper.jserve.web.sql.export.SqlTextExportService;
import com.jhelper.jserve.web.sql.model.QueryVO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sql-export")
public class SqlExportController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SqlHelperService sqlHelperService;

    @Autowired
    SqlExcelExportService sqlExcelExportService;

    @Autowired
    SqlTextExportService sqlTextExportService;

    @Autowired
    SqlJsonExportService sqlJsonExportService;

    @PostMapping("/excel")
    public ResponseEntity<Resource> exportExcel(@RequestBody QueryVO queryVo) throws IOException {
        File file = sqlExcelExportService.export(queryVo);

        Resource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"sql_export.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/text")
    public ResponseEntity<Resource> exportText(@RequestBody QueryVO queryVo) throws IOException {
        File file = sqlTextExportService.export(queryVo);

        Resource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"sql_export.txt\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/json")
    public ResponseEntity<Resource> exportJson(@RequestBody QueryVO queryVo) throws IOException {
        File file = sqlJsonExportService.export(queryVo);

        Resource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"sql_export.txt\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
