package com.jhelper.jserve.web.sql.model;

import lombok.Data;

@Data
public class SqlError {
    private String sqlState;
    private String errorMessage;
}
