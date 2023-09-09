package com.jhelper.jserve.web.sql.model;

import lombok.Data;

@Data
public class SqlResult {
    private String[] columnNames;
    private Object[][] result;
    private boolean hasNext = false;
    private int count;

    public SqlResult() {
        columnNames = new String[0];
        result = new Object[0][0];
    }
}
