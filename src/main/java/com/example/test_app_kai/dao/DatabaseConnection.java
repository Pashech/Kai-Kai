package com.example.test_app_kai.dao;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DatabaseConnection {

    @Getter
    private Connection connection;

    @Value("${datasource.url}")
    private String dbUrl;
    @Value("${datasource.username}")
    private String dbUser;
    @Value("${datasource.password.dev}")
    private String dbPassword;

    @PostConstruct
    public void init() {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    gfdhkhkhdkfjg
//    dfgdkljgdklfjg
//            dfgkld;flgkd;flgk
}
