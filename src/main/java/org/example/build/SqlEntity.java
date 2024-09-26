package org.example.build;

public class SqlEntity {

    private String id;
    private String resultType;
    private String sql;
    private String parameterType;
    private SqlStatementType statementType;

    public SqlEntity(String id, String resultType, String sql, String parameterType,
                     SqlStatementType statementType) {
        this.id = id;
        this.resultType = resultType;
        this.sql = sql;
        this.parameterType = parameterType;
        this.statementType = statementType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public SqlStatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(SqlStatementType statementType) {
        this.statementType = statementType;
    }

    @Override
    public String toString() {
        return "SqlEntity{" +
                "id='" + id + '\'' +
                ", resultType='" + resultType + '\'' +
                ", sql='" + sql + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", statementType=" + statementType +
                '}';
    }
}
