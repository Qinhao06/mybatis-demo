package org.example.build;

import org.example.logging.Logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SqlBuilder {



    public static String buildSql(SqlEntity sqlEntity, Object[] args, Method method)
    {
        String sqlBeforeHandled = sqlEntity.getSql();
        Map<String, Object> paramsMap = buildArgs(args, method);
        return replacePlaceholders(sqlBeforeHandled, paramsMap);
    }

    public static String replacePlaceholders(String sql, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "#{" + entry.getKey() + "}";
            Object value = entry.getValue();

            // 如果值是字符串，使用单引号包裹
            if (value instanceof String) {
                sql = sql.replace(placeholder, "'" + value + "'");
            } else {
                sql = sql.replace(placeholder, String.valueOf(value));
            }
        }
        return sql;
    }

    private static Map<String, Object> buildArgs(Object[] args, Method method)
    {
        Map<String, Object> params = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();
            Param annotation = parameter.getAnnotation(Param.class);
            Object arg = args[i];
            if (type.isPrimitive() ||
                    IsDefaultParameter(type) || type.isAssignableFrom(String.class)) {
                Optional.ofNullable(annotation).ifPresentOrElse(
                        param -> params.put(param.value(), arg),
                        () -> params.put(parameter.getName(),arg)
                );
            }else{
                if (!arg.getClass().isAssignableFrom(type)) {
                    Logging.logger.error("Parameter type mismatch");
                    throw new RuntimeException("Parameter type mismatch");
                }
                Field[] declaredFields = type.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    try {
                        Object filedValue = field.get(arg);
                        params.put(field.getName(), filedValue);
                        Optional.ofNullable(annotation).ifPresent(
                                param -> params.put(param.value() + "." + field.getName(), filedValue)
                        );
                    } catch (IllegalAccessException e) {
                        Logging.logger.error("Parameter type mismatch");
                        throw new RuntimeException("Parameter type mismatch" + e);
                    }
                }

            }

        }
        return params;
    }

    private static  boolean IsDefaultParameter(Object obj){
        return obj instanceof Integer || obj instanceof Byte || obj instanceof Short ||
                obj instanceof Long || obj instanceof Float || obj instanceof Double ||
                obj instanceof Character || obj instanceof Boolean;
    }

}
