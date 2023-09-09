package com.jhelper.jserve.web.sql.model;

import lombok.Data;

@Data
public class QueryVO {
    private String query;
    private String name;
    private String[] params;
    private int fetchSize = -1;
}
