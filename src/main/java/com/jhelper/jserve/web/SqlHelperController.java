package com.jhelper.jserve.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.jhelper.jserve.web.entity.SqlVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sql")
public class SqlHelperController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("sqlHelperJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public SqlVO select(final String sql, String[] params) {

        logger.debug("sql: {}", sql);

        List<String[]> resultList = new ArrayList<String[]>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, params);

        SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();

        final String[] columnNames = sqlRowSetMetaData.getColumnNames();
        final int columnSize = columnNames.length;

        while (sqlRowSet.next()) {

            String[] columns = new String[columnSize];

            for (int i = 0; i < columnSize; i++) {
                columns[i] = sqlRowSet.getString(i + 1);
            }

            resultList.add(columns);
        }

        SqlVO sqlVO = new SqlVO();
        sqlVO.setColumnNames(columnNames);
        sqlVO.setResult(resultList.toArray(new String[0][]));

        return sqlVO;
    }

    @PostMapping
    public SqlVO query(@RequestBody Map<String, ?> params) {

        String query = Objects.toString(params.get("query"));
        String[] data = (String[]) params.get("data");

        return select(query, data);
    }
}
