package com.wpg.demo.spring.springframework.demo03;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author ChangWei Li
 * @version 2017-08-25 15:00
 */
@Configuration
@ComponentScan
public class JdbcApplication {

    private static final Logger logger = LoggerFactory.getLogger(JdbcApplication.class);

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost/waterdb_eastchina?useSSL=false");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("a767940334");

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JdbcApplication.class);

        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);

        List<Map<String, Object>> resultList = jdbcTemplate.queryForList("SELECT current_timestamp");

        logger.debug(resultList.toString());
    }

}
