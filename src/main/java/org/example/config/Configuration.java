package org.example.config;

import org.example.config.xml.XMLConfigReader;

import java.util.List;
import java.util.Map;

public class Configuration {

    private String environmentID;

    private String transactionManagerType;

    private String dataSourceType;

    private String url;

    private String username;

    private String password;

    private String driver;

    private Map<String, String> typeAliases;

    private List<String> mapperPaths;

    private Map<String, MapperConfiguration> mappers;

    private static volatile Configuration configuration;

    public static Configuration getConfiguration(String path) {
        BaseConfigReader configReader = new XMLConfigReader();
        return configReader.getConfigProperties(path);
    }

    public static Configuration getConfiguration(){
        if (configuration == null){
            synchronized (Configuration.class){
                if (configuration == null){
                    BaseConfigReader configReader = new XMLConfigReader();
                    configuration =  configReader.getConfigProperties();
                }
            }
        }
        return configuration;
    }

    public Configuration() {
    }

    public String getEnvironmentID() {
        return environmentID;
    }

    public void setEnvironmentID(String environmentID) {
        this.environmentID = environmentID;
    }

    public String getTransactionManagerType() {
        return transactionManagerType;
    }

    public void setTransactionManagerType(String transactionManagerType) {
        this.transactionManagerType = transactionManagerType;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Map<String, String> getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(Map<String, String> typeAliases) {
        this.typeAliases = typeAliases;
    }

    public List<String> getMapperPaths() {
        return mapperPaths;
    }

    public void setMapperPaths(List<String> mapperPaths) {
        this.mapperPaths = mapperPaths;
    }

    public Map<String, MapperConfiguration> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, MapperConfiguration> mappers) {
        this.mappers = mappers;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "environmentID='" + environmentID + '\'' +
                ", transactionManagerType='" + transactionManagerType + '\'' +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", driver='" + driver + '\'' +
                ", typeAliases=" + typeAliases +
                ", mapperPaths=" + mapperPaths +
                ", mappers=" + mappers +
                '}';
    }
}
