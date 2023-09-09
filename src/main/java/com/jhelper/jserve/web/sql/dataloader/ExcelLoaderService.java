package com.jhelper.jserve.web.sql.dataloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jhelper.export.ExcelReader;
import com.jhelper.export.RowReadHandler;
import com.jhelper.jserve.web.sql.dataloader.model.ExcelLoaderVO;
import com.jhelper.jserve.web.sql.jdbc.JdbcTemplateManager;
import com.jhelper.store.Store;

@Service
public class ExcelLoaderService {

    @Autowired
    JdbcTemplateManager jdbcManager;

    @Autowired
    Store store;

    public void load(ExcelLoaderVO excelLoaderVO) {

        ExcelReader excelReader = new ExcelReader();
        Loader loader = new Loader(excelLoaderVO.getTargetName(),
                excelLoaderVO.getTargetOwner(),
                excelLoaderVO.getTargetTableName(),
                excelLoaderVO.getTargetColumns(),
                excelLoaderVO.getQueryParams());

        try {

            final int startRow = excelLoaderVO.getStartRow();
            final int startCol = excelLoaderVO.getStartCol();

            excelReader.read(store.getPath(excelLoaderVO.getPath()).toFile(), new RowReadHandler() {
                @Override
                public void cellValues(int rowNum, Object[] values) {

                    if (rowNum < startRow) {
                        return;
                    }

                    if (startCol > 0) {
                        values = Arrays.copyOfRange(values, startCol, values.length);
                    }

                    loader.insert(values);
                }
            });
        } catch (IOException e) {
        }
    }

    class Loader {

        String name;
        String owner;
        String tableName;
        String[] columns;
        String queryParams;

        public Loader(String name,
                String owner,
                String tableName,
                String[] columns,
                String queryParams) {
            this.name = name;
            this.owner = owner;
            this.tableName = tableName;
            this.columns = columns;
            this.queryParams = queryParams;
        }

        public void insert(Object[] values) {

            final JdbcTemplate jdbcTemplate = jdbcManager.getJdbcTemplateById(this.name);
            final String statement = getStatement();

            int columnsLength = this.columns.length;
            List<Object[]> paramsTemp = new ArrayList<>();

            Object[] params = new Object[columnsLength];

            for (int i = 0; i < columnsLength; i++) {
                params[i] = values[i];
            }

            paramsTemp.add(params);

            jdbcTemplate.update(statement, params);
            paramsTemp.clear();
        }

        public String getStatement() {
            String owner = this.owner;
            String tableName = this.tableName;
            String[] columns = this.columns;

            String statement = String.format("insert into %s.%s(%s) values(%s)",
                    owner,
                    tableName,
                    String.join(", ", columns),
                    queryParams);

            return statement;
        }
    }
}
