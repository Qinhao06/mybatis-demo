package org.example.config;

public interface BaseConfigReader {

    Configuration getConfigProperties(String path);

    Configuration getConfigProperties();

}
