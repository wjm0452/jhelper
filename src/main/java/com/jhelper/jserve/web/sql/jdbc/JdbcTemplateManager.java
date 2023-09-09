package com.jhelper.jserve.web.sql.jdbc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.jhelper.jserve.web.entity.ConnInfo;
import com.jhelper.jserve.web.sql.ConnInfoService;

@Component
public class JdbcTemplateManager {

    Map<String, JdbcTemplate> jdbcTemplates = new HashMap<>();

    @Autowired
    ConnInfoService connInfoService;

    public void addJdbcTemplate(ConnInfo connInfo) {
        synchronized (jdbcTemplates) {
            DataSource dataSource = DataSourceBuilder.create()
                    .url(connInfo.getJdbcUrl())
                    .driverClassName(connInfo.getDriverClassName())
                    .username(connInfo.getUsername())
                    .password(connInfo.getPassword())
                    .build();

            JdbcTemplate jdbcTmpl = new JdbcTemplate(dataSource);

            jdbcTemplates.put(connInfo.getName(), jdbcTmpl);
        }
    }

    public void removeJdbcTemplate(String id) {
        synchronized (jdbcTemplates) {
            jdbcTemplates.remove(id);
        }
    }

    public JdbcTemplate getJdbcTemplateById(String id) {
        JdbcTemplate jdbcTemplate = jdbcTemplates.get(id);

        if (jdbcTemplate == null) {
            ConnInfo connInfo = connInfoService.findById(id);

            if (connInfo == null) {
                return null;
            }

            addJdbcTemplate(connInfo);

            jdbcTemplate = jdbcTemplates.get(id);
        }

        return jdbcTemplate;
    }
}
