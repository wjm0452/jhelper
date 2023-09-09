package com.jhelper.jserve.web.sql.export;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import com.jhelper.export.excel.SimpleExcelExporter;
import com.jhelper.jserve.web.sql.ResultSetHandler;
import com.jhelper.jserve.web.sql.SqlExportService;
import com.jhelper.jserve.web.sql.SqlHelperService;
import com.jhelper.jserve.web.sql.model.QueryVO;

@Service
public class SqlExcelExportService implements SqlExportService {

    @Autowired
    SqlHelperService sqlHelperService;

    public File export(QueryVO queryVO) throws IOException {

        try (final SimpleExcelExporter simpleExcelExporter = new SimpleExcelExporter();) {

            sqlHelperService.select(queryVO, new ResultSetHandler() {
                @Override
                public void process(ResultSet rs) throws SQLException {

                    ResultSetWrappingSqlRowSet sqlRowSet = new ResultSetWrappingSqlRowSet(rs);
                    SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();

                    final String[] columnNames = sqlRowSetMetaData.getColumnNames();
                    final int columnSize = columnNames.length;

                    simpleExcelExporter.writeHead(columnNames);

                    while (sqlRowSet.next()) {

                        Object[] columns = new Object[columnSize];

                        for (int i = 0; i < columnSize; i++) {
                            columns[i] = sqlRowSet.getObject(i + 1);
                        }

                        simpleExcelExporter.writeData(simpleExcelExporter.toSimpleCell(columns));
                    }

                    for (int i = 0; i < columnSize; i++) {
                        simpleExcelExporter.getExcelWriter().autoSizeColumn(i);
                    }
                }
            });

            return simpleExcelExporter.export();
        }
    }
}
