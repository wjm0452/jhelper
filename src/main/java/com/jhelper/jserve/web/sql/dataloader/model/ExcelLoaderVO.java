package com.jhelper.jserve.web.sql.dataloader.model;

import lombok.Data;

@Data
public class ExcelLoaderVO {
    private String path;
    private String targetName;
    private String targetOwner;
    private String targetTableName;
    private String[] targetColumns;
    private int startRow = 0;
    private int startCol = 0;
    private String queryParams;
}
