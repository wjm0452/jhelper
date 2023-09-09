package com.jhelper.jserve.web;

import com.jhelper.jserve.web.sql.SqlHelperService;
import com.jhelper.jserve.web.sql.model.QueryVO;
import com.jhelper.jserve.web.sql.model.SqlError;
import com.jhelper.jserve.web.sql.model.SqlResult;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sql")
public class SqlHelperController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SqlHelperService sqlHelperService;

    @ExceptionHandler(SQLServerException.class)
    public ResponseEntity<SqlError> sqlServerError(SQLServerException e) {

        SqlError sqlError = new SqlError();

        sqlError.setSqlState(e.getSQLState());
        sqlError.setErrorMessage(e.getSQLServerError().getErrorMessage());

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(sqlError);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SqlError> runtimeError(RuntimeException e) {

        SqlError sqlError = new SqlError();

        sqlError.setSqlState("RUN");
        sqlError.setErrorMessage(e.getMessage());

        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(sqlError);
    }

    @PostMapping
    public SqlResult query(@RequestBody QueryVO queryVo) throws SQLServerException {
        try {
            return sqlHelperService.execute(queryVo);
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();

            if (cause instanceof SQLServerException) {
                SQLServerException sqlServerException = ((SQLServerException) cause);
                throw sqlServerException;
            }

            throw e;
        }
    }
}
