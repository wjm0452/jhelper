package com.jhelper.jserve.web.entity;

import lombok.Data;

@Data
public class SqlVO {
    private String scheme;
    private String tableName;
    private String[] columnNames;
    private String[][] result;
}
