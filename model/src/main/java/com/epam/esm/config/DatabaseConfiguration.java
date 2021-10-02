package com.epam.esm.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySource("classpath:properties/db.properties")
public class DatabaseConfiguration {

    @Value("driver")
    private String driverName;
    @Value("user")
    private String username;
    @Value("password")
    private String password;
    @Value("url")
    private String url;
    @Value("db.pool.min")
    private int minimumPoolSize;
    @Value("db.pool.max")
    private int maximumPoolSize;

    @Bean
    public DataSource mySqlData() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
