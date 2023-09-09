package com.jhelper.jserve.web.sql;

import java.io.File;
import java.io.IOException;

import com.jhelper.jserve.web.sql.model.QueryVO;

public interface SqlExportService {
    public File export(QueryVO queryVO) throws IOException;
}
