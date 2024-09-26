package org.example.proxy;

import org.example.build.SqlBuilder;
import org.example.build.SqlStatementType;
import org.example.config.Configuration;
import org.example.config.MapperConfiguration;
import org.example.build.SqlEntity;
import org.example.connection.BaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class MapperProxy implements InvocationHandler {
    private final MapperConfiguration mapperConfiguration;

    private final Configuration configuration;

    private final BaseConnectionManager connectionManager;

    public MapperProxy(MapperConfiguration mapperConfiguration,
                       Configuration configuration, BaseConnectionManager connectionManager) {
        this.mapperConfiguration = mapperConfiguration;
        this.configuration = configuration;
        this.connectionManager = connectionManager;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        SqlEntity sqlEntity = mapperConfiguration.getSqlStatements().get(methodName);
        Optional.ofNullable(sqlEntity).orElseThrow(
                (Supplier<Throwable>) () -> new RuntimeException("No such method in mapper" + mapperConfiguration.getNamespace())
        );
        String sql = SqlBuilder.buildSql(sqlEntity, args, method);
        return executeSql(sql, method);
    }

    private Object executeSql(String sql, Method method) {

        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            System.out.println(connection);
            Statement statement = connection.createStatement();
            statement.execute(sql);

            Map<String, String> typeAliases = configuration.getTypeAliases();
            Map<String, SqlEntity> sqlStatements = mapperConfiguration.getSqlStatements();
            SqlEntity sqlEntity = sqlStatements.get(method.getName());
            if (sqlEntity.getStatementType() != SqlStatementType.SELECT){
                return statement.getUpdateCount();
            }
            ResultSet resultSet = statement.getResultSet();
            String resultType = sqlEntity.getResultType();
            Class<?> methodReturnType = method.getReturnType();
            if(resultType != null && !resultType.isEmpty()){
                if(typeAliases.containsKey(resultType)){
                    resultType = typeAliases.get(resultType);
                }
            }else{
                resultType = methodReturnType.getName();
            }
            Class<?> resultTypeClass = Class.forName(resultType);
            int columnCount = resultSet.getMetaData().getColumnCount();
            if (methodReturnType.isAssignableFrom(List.class)){
                List<Object> resultList = new ArrayList<>();
                while (resultSet.next()) {
                    Object result = resultTypeClass.getConstructors()[0].newInstance();
                    extracted(columnCount, resultSet, resultTypeClass, result);
                    resultList.add(result);
                }
                return resultList;
            }else{
                if(resultSet.next()){
                    Object result = resultTypeClass.getConstructors()[0].newInstance();
                    extracted(columnCount, resultSet, resultTypeClass, result);
                    return result;
                }
                return null;
            }

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null) {
                connectionManager.releaseConnection(connection);
            }
        }
    }

    private static void extracted(int columnCount, ResultSet resultSet, Class<?> resultTypeClass, Object result) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method[] methods = resultTypeClass.getMethods();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            Object value = resultSet.getObject(columnName);
            for (Method method : methods) {
                String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                if (method.getName().equals(methodName)) {
                    method.invoke(result, value);
                    break;
                }
            }
        }
    }


}

