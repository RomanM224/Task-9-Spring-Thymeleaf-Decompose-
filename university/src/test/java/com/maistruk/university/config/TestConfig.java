package com.maistruk.university.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan("com.maistruk.university.dao")
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql")
                .build();
        return dataSource;
    }
    
    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
