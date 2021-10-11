package com.epam.esm.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
@PropertySource("classpath:properties/db-prod.properties")
public class DatabaseConfiguration {

    @Value("${database.driver}")
    private String driverName;
    @Value("${database.user}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.url}")
    private String url;
    @Value("${database.pool.min}")
    private int minimumPool;
    @Value("${database.pool.max}")
    private int maximumPool;

    @Bean
    public DataSource customSqlData() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(minimumPool);
        dataSource.setMaxIdle(maximumPool);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
