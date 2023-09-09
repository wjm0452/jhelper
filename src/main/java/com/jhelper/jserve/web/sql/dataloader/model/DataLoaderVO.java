package com.jhelper.jserve.web.sql.dataloader.model;

import lombok.Data;

@Data
public class DataLoaderVO {

    private String sourceName;
    private String sourceOwner;
    private String sourceTableName;
    private String sourceQuery;

    private String targetName;
    private String targetOwner;
    private String targetTableName;
    private String[] targetColumns;
}
