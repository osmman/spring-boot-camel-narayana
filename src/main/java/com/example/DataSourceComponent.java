package com.example;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.XADataSource;

@Component
public class DataSourceComponent {

    @Bean
    public XADataSource xaDataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String password) {
        PGXADataSource xa = new PGXADataSource();
        xa.setUser(user);
        xa.setPassword(password);
        xa.setUrl(url);


        org.apache.tomcat.jdbc.pool.XADataSource ds = new org.apache.tomcat.jdbc.pool.XADataSource();
        ds.setPassword(password);
        ds.setUsername(user);
        ds.setDataSource(xa);
        return ds;
    }
}
