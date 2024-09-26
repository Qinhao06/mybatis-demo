package org.example.connection;

import org.example.config.Configuration;

import java.sql.Connection;

public abstract class BaseConnectionManager {

    protected Configuration configuration;

    protected String configPath;



    public BaseConnectionManager(String configPath) {
        this.configPath = configPath;
        configuration = Configuration.getConfiguration(configPath);
    }

    public BaseConnectionManager() {
        configuration = Configuration.getConfiguration();
    }


    public abstract Connection getConnection();

    public abstract void releaseConnection(Connection connection);

}
