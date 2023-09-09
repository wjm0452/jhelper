package com.jhelper.jserve.web.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {

    public void process(ResultSet rs) throws SQLException;

}
