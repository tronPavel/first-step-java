package org.example.demo.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.demo.config.DBConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final Logger logger = LogManager.getLogger(DBConnection.class);
    private static final DataSource dataSource;

    static {
        try {
            logger.info("Initializing HikariCP connection pool...");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DBConfig.get("db.url"));
            config.setUsername(DBConfig.get("db.username"));
            config.setPassword(DBConfig.get("db.password"));
            config.setDriverClassName(DBConfig.get("db.driver"));
            config.setMaximumPoolSize(Integer.parseInt(DBConfig.get("db.maxPoolSize")));
            config.setMinimumIdle(Integer.parseInt(DBConfig.get("db.minIdle")));
            config.setConnectionTimeout(Long.parseLong(DBConfig.get("db.connectionTimeout")));
            config.setIdleTimeout(Long.parseLong(DBConfig.get("db.idleTimeout")));
            config.setMaxLifetime(Long.parseLong(DBConfig.get("db.maxLifetime")));
            config.setPoolName("MyHikariPool");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
            logger.info("Connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize connection pool", e);
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        logger.debug("Obtaining connection from pool");
        Connection rawConnection = dataSource.getConnection();
        return ConnectionProxy.createProxy(rawConnection, dataSource);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}