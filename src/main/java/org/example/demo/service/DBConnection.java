package org.example.demo.service;

import org.example.demo.config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    DBConfig.get("db.url"),
                    DBConfig.get("db.username"),
                    DBConfig.get("db.password")
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Ошибка загрузки драйвера", e);
        }
    }
}


