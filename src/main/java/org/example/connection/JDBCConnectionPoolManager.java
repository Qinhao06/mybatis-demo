package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;

public class JDBCConnectionPoolManager extends BaseConnectionManager{

    private final String url = configuration.getUrl();
    private final String username = configuration.getUsername();
    private final String password = configuration.getPassword();

    private final LinkedBlockingDeque<Connection> connectionPool;

    private final int maxPoolSize;

    private final int minPoolSize;

    private int nowPoolSize;

    public JDBCConnectionPoolManager(int maxPoolSize, int minPoolSize ) {
        super();
        connectionPool = new LinkedBlockingDeque<>();
        this.maxPoolSize = maxPoolSize;
        this.minPoolSize = minPoolSize;
        nowPoolSize = minPoolSize;
        for (int i = 0; i < minPoolSize; i++) {
            try {
                connectionPool.add(DriverManager.getConnection(url, username, password));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        synchronized (JDBCConnectionPoolManager.class){
            if (connectionPool.isEmpty() && nowPoolSize < maxPoolSize){
                nowPoolSize++;
                try {
                    connectionPool.putFirst(DriverManager.getConnection(url, username, password));
                } catch (InterruptedException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            return connectionPool.takeFirst();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void releaseConnection(Connection connection) {
        synchronized (JDBCConnectionPoolManager.class){
            if (!connectionPool.isEmpty() && nowPoolSize > minPoolSize){
                nowPoolSize--;
                try {
                    connection.close();
                    return;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            connectionPool.putFirst(connection);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
