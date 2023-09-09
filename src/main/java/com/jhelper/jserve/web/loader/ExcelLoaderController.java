package com.jhelper.jserve.web.loader;

import com.jhelper.export.ExcelReader;
import com.jhelper.export.RowReadHandler;
import com.jhelper.jserve.web.sql.dataloader.ExcelLoaderService;
import com.jhelper.jserve.web.sql.dataloader.model.ExcelLoaderVO;
import com.jhelper.store.Store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/dataloader")
public class ExcelLoaderController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ExcelLoaderService excelLoaderService;

    @Autowired
    Store store;

    @PostMapping("/excel")
    public void excelloader(@RequestBody ExcelLoaderVO excelLoaderVO) throws IOException {
        excelLoaderService.load(excelLoaderVO);
    }

    @PostMapping("/excel-file")
    public ResponseEntity<Map<String, ?>> excelFile(@RequestPart("file") MultipartFile multipartFile)
            throws IOException {
        Path tempFile = Files.createTempFile(store.getPath(), "", "");
        multipartFile.transferTo(tempFile);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("path", store.getPath().relativize(tempFile).toString());

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultMap);
    }

    @GetMapping("/excel-file")
    public ResponseEntity<Map<String, ?>> excelFile(@RequestParam("path") final String path,
            @RequestParam(name = "offset", defaultValue = "0") final int offset,
            @RequestParam(name = "limit", defaultValue = "100") final int limit) throws IOException {

        Path excelPath = store.getPath(path);

        ExcelReader excelReader = new ExcelReader();

        List<Object[]> rows = new ArrayList<>();

        try {
            excelReader.read(excelPath.toFile(), new RowReadHandler() {
                @Override
                public void cellValues(int rowNum, Object[] values) {

                    if (rowNum >= offset && rowNum < limit) {
                        rows.add(values);
                    }

                    if (rowNum >= limit) {
                        throw new RuntimeException("row_size_limit_exceeded");
                    }
                }
            });
        } catch (RuntimeException e) {
            // row_size_limit_exceeded
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("offset", offset);
        resultMap.put("limit", limit);
        resultMap.put("result", rows);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resultMap);
    }
}
