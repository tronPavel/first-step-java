package org.example.demo.service;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProxy implements InvocationHandler {
    private final Connection connection;
    private final DataSource dataSource;

    private ConnectionProxy(Connection connection, DataSource dataSource) {
        this.connection = connection;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            if (!connection.isClosed()) {
                connection.close(); // HikariCP handles returning to pool
            }
            return null;
        }
        return method.invoke(connection, args);
    }

    public static Connection createProxy(Connection connection, DataSource dataSource) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionProxy(connection, dataSource)
        );
    }
}