package org.example.config;

import org.example.build.SqlEntity;

import java.util.HashMap;
import java.util.Map;

public class MapperConfiguration {

    private String namespace;
    private Map<String, SqlEntity> sqlStatements;

    public MapperConfiguration(String namespace, Map<String, SqlEntity> sqlStatements) {
        this.namespace = namespace;
        this.sqlStatements = sqlStatements;
    }

    public MapperConfiguration() {
        this.sqlStatements = new HashMap<>();
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, SqlEntity> getSqlStatements() {
        return sqlStatements;
    }

    public void setSqlStatements(Map<String, SqlEntity> sqlStatements) {
        this.sqlStatements = sqlStatements;
    }

    @Override
    public String toString() {
        return "MapperConfiguration{" +
                "namespace='" + namespace + '\'' +
                ", sqlStatements=" + sqlStatements +
                '}';
    }
}
