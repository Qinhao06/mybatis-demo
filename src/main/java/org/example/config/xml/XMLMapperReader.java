package org.example.config.xml;

import org.example.config.BaseMapperReader;
import org.example.config.MapperConfiguration;
import org.example.build.SqlEntity;
import org.example.build.SqlStatementType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Optional;

public class XMLMapperReader extends BaseXMLReader implements BaseMapperReader {

    private final String path;

    public XMLMapperReader(String path) {
        this.path = path;
    }

    public MapperConfiguration getMapperProperties() {
        Document doc = readXml(path);
        Optional.ofNullable(doc).orElseThrow(() -> new RuntimeException("error in reading mapper xml file"));
        MapperConfiguration properties = new MapperConfiguration();
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        String namespace = root.getAttribute("namespace");
        if(namespace.isEmpty()){
            logger.error(path + " namespace is empty or wrong");
            throw new RuntimeException("mapper " + path +" namespace is empty or wrong");
        }
        properties.setNamespace(namespace);

        getSqlEntities(root, properties, "select");
        getSqlEntities(root, properties, "insert");
        getSqlEntities(root, properties, "update");
        getSqlEntities(root, properties, "delete");

        return properties;
    }


    private void getSqlEntities(Element root, MapperConfiguration properties, String type){
        NodeList selects = root.getElementsByTagName(type);

        for (int i = 0; i < selects.getLength(); i++) {
            Element element = (Element) selects.item(i);
            String id = element.getAttribute("id");
            String resultType = element.getAttribute("resultType");
            String parameterType = element.getAttribute("parameterType");
            String sql = element.getTextContent();
            SqlStatementType statementType = SqlStatementType.valueOf(type.toUpperCase());
            SqlEntity sqlEntity = new SqlEntity(id, resultType, sql, parameterType, statementType);
            properties.getSqlStatements().put(id, sqlEntity);
        }
    }






}

