package com.side.community.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    private final ConfigVariable configVariable;

    public DataSourceConfig(ConfigVariable configVariable) {
        this.configVariable = configVariable;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(configVariable.getDriver());
        dataSource.setUrl(configVariable.getUrl());
        dataSource.setUsername(configVariable.getUserId());
        dataSource.setPassword(configVariable.getPassword());
        return dataSource;
    }
}
