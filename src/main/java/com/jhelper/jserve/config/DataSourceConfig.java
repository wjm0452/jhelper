package com.jhelper.jserve.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("sqlHelperDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sql-helper")
    public DataSource sqlHelperDataSource() {
        return DataSourceBuilder.create().build();
    }
}
