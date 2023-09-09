package com.jhelper.jserve.web.sql.dataloader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.jhelper.jserve.web.sql.dataloader.model.DataLoaderVO;
import com.jhelper.jserve.web.sql.jdbc.JdbcTemplateManager;

@Service
public class DataLoaderService {

    @Autowired
    JdbcTemplateManager jdbcManager;

    public void load(DataLoaderVO dataLoaderVO) {
        String query = dataLoaderVO.getSourceQuery();
        JdbcTemplate sourceJdbc = jdbcManager.getJdbcTemplateById(dataLoaderVO.getSourceName());

        Loader loader = new Loader(dataLoaderVO.getTargetName(),
                dataLoaderVO.getTargetOwner(),
                dataLoaderVO.getTargetTableName(),
                dataLoaderVO.getTargetColumns());

        sourceJdbc.query(query, new ResultSetExtractor<Object>() {

            @Override
            @Nullable
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                loader.load(rs);
                return null;
            }
        });
    }

    class Loader {

        String name;
        String owner;
        String tableName;
        String[] columns;

        public Loader(String name,
                String owner,
                String tableName,
                String[] columns) {
            this.name = name;
            this.owner = owner;
            this.tableName = tableName;
            this.columns = columns;
        }

        public void load(ResultSet rs) throws SQLException {
            final JdbcTemplate jdbcTemplate = jdbcManager.getJdbcTemplateById(this.name);
            final String statement = getStatement();

            int columnsLength = this.columns.length;
            int count = 0;
            List<Object[]> paramsTemp = new ArrayList<>();

            while (rs.next()) {

                Object[] params = new Object[columnsLength];

                for (int i = 0; i < columnsLength; i++) {
                    params[i] = rs.getObject(i + 1);
                }

                paramsTemp.add(params);

                count++;

                if (count >= 100) {
                    jdbcTemplate.batchUpdate(statement, paramsTemp);
                    paramsTemp.clear();
                    count = 0;
                }
            }

            if (count > 0) {
                jdbcTemplate.batchUpdate(statement, paramsTemp);
                paramsTemp.clear();
                count = 0;
            }

        }

        public String getStatement() {
            String owner = this.owner;
            String tableName = this.tableName;
            String[] columns = this.columns;

            String[] params = new String[columns.length];
            Arrays.fill(params, 0, params.length, "?");

            String statement = String.format("insert into %s.%s(%s) values(%s)",
                    owner,
                    tableName,
                    String.join(", ", columns),
                    String.join(", ", params));

            return statement;
        }
    }
}
