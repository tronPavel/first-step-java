package org.example.demo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//public class DBConnection {
//    private static Connection connection;
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection(
//                    DBConfig.get("db.url"),
//                    DBConfig.get("db.username"),
//                    DBConfig.get("db.password")
//            );
//        } catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException("Ошибка подключения к базе данных", e);
//        }
//    }
//
//    public static Connection getConnection() {
//        System.out.println("Получаем соединение с БД");
//        return connection;
//    }
//}

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


