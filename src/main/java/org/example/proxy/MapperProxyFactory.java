package org.example.proxy;

import org.example.config.Configuration;
import org.example.config.MapperConfiguration;
import org.example.connection.BaseConnectionManager;

import java.lang.reflect.Proxy;
import java.util.Map;

public class MapperProxyFactory {

    private final Configuration configuration;

    private final BaseConnectionManager connectionManager;

    public MapperProxyFactory(Configuration configuration, BaseConnectionManager connectionManager) {
        this.configuration = configuration;
        this.connectionManager = connectionManager;
    }

    public Object  getMapperProxy(Class<?> targetClass){
        Map<String, MapperConfiguration> mappers = configuration.getMappers();
        if(!mappers.containsKey(targetClass.getName())){
            throw new RuntimeException("Mapper namespace not found");
        }
        MapperConfiguration mapperConfiguration = mappers.get(targetClass.getName());
        return Proxy.newProxyInstance(targetClass.getClassLoader(),
                new Class[]{targetClass}, new MapperProxy(mapperConfiguration, configuration, connectionManager));

    }


}
