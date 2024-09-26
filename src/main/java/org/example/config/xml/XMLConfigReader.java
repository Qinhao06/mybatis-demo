package org.example.config.xml;

import org.example.config.BaseConfigReader;
import org.example.config.Configuration;
import org.example.config.MapperConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;

public class XMLConfigReader extends BaseXMLReader implements BaseConfigReader {

    private static final String XML_PATH = "src/main/resources/mybatis-config.xml";

    @Override
    public Configuration getConfigProperties() {
        return getConfigProperties(XML_PATH);
    }

    public Configuration getConfigProperties(String path)
    {
        Document doc = readXml(path);
        Optional.ofNullable(doc).orElseThrow(() -> new RuntimeException("error in reading xml file"));
        Configuration properties = new Configuration();
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        getEnvironmentsConfig(root, properties);
        getTypeAliases(root, properties);
        getMappers(root, properties);
        return properties;
    }

    private void getEnvironmentsConfig(Element root, Configuration properties ){
        NodeList environmentsList = root.getElementsByTagName("environments");
        if (environmentsList.getLength() != 1) {
            throw new RuntimeException("error in reading the environment of xml file");
        }

        Element environments = (Element) environmentsList.item(0);

        NodeList environmentList = environments.getElementsByTagName("environment");
        Element environment = (Element) environmentList.item(0);

        String environmentID = environment.getAttribute("id");
        properties.setEnvironmentID(environmentID);

        NodeList dataSource = environment.getElementsByTagName("dataSource");
        if (dataSource.getLength() != 1) {
            throw new RuntimeException("error in reading the dataSource of xml file");
        }
        Element dataSourceElement = (Element) dataSource.item(0);

        String dataSourceType = dataSourceElement.getAttribute("type");
        properties.setDataSourceType(dataSourceType);

        NodeList datasourcePropertyList = dataSourceElement.getElementsByTagName("property");
        if (datasourcePropertyList.getLength() != 4) {
            throw new RuntimeException("error in reading the dataSource of xml file");
        }
        for (int i = 0; i < datasourcePropertyList.getLength(); i++) {
            Element element = (Element) datasourcePropertyList.item(i);
            String Name = element.getAttribute("name");
            String value = element.getAttribute("value");
            switch (Name){
                case "driver":
                    properties.setDriver(value);
                    break;
                case "url":
                    properties.setUrl(value);
                    break;
                case "username":
                    properties.setUsername(value);
                    break;
                case "password":
                    properties.setPassword(value);
                    break;
                default:
                    throw new RuntimeException("error in reading the dataSource ofxml file");
            }
        }

        NodeList transactionManager = environment.getElementsByTagName("transactionManager");
        if (transactionManager.getLength() >= 1) {
            Element item = (Element)transactionManager.item(0);
            properties.setTransactionManagerType(item.getAttribute("type"));
        }
    }

    private void getTypeAliases(Element root, Configuration properties){
        NodeList typeAliases = root.getElementsByTagName("typeAliases");
        Map<String, String> typeAliasesMap = new HashMap<>();
        for (int i = 0; i < typeAliases.getLength(); i++) {
            Element element = (Element) typeAliases.item(i);
            NodeList typeAlias = element.getElementsByTagName("typeAlias");
            for (int j = 0; j < typeAlias.getLength(); j++) {
                Element typeAliasElement = (Element) typeAlias.item(j);
                String alias = typeAliasElement.getAttribute("alias");
                String type = typeAliasElement.getAttribute("type");
                typeAliasesMap.put( alias, type);
            }
        }
        properties.setTypeAliases(typeAliasesMap);
    }

    private void getMappers(Element root, Configuration properties){

        NodeList mappers = root.getElementsByTagName("mapperPaths");
        List<String> mappersList = new ArrayList<>();
        for (int i = 0; i < mappers.getLength(); i++) {
            Element element = (Element) mappers.item(i);
            NodeList mapper = element.getElementsByTagName("mapper");
            for (int j = 0; j < mapper.getLength(); j++) {
                Element mapperElement = (Element) mapper.item(j);
                String resource = mapperElement.getAttribute("resource");
                mappersList.add(resource);
            }

        }
        properties.setMapperPaths(mappersList);
        Map<String, MapperConfiguration> mapperConfigurations = new HashMap<>();
        for (String mapperPath : mappersList) {
            XMLMapperReader xmlMapperReader = new XMLMapperReader(mapperPath);
            MapperConfiguration mapperConfiguration = xmlMapperReader.getMapperProperties();
            mapperConfigurations.put(mapperConfiguration.getNamespace(), mapperConfiguration);
        }
        properties.setMappers(mapperConfigurations);
    }


}
