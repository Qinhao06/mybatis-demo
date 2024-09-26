import org.example.config.Configuration;
import org.example.config.MapperConfiguration;
import org.example.config.xml.XMLConfigReader;
import org.example.config.xml.XMLMapperReader;
import org.junit.Before;
import org.junit.Test;

public class TestForXML {


    private XMLConfigReader xmlConfigReader ;

    private XMLMapperReader xmlMapperReader ;

    @Before
    public void init(){
        xmlConfigReader = new XMLConfigReader();
        xmlMapperReader = new XMLMapperReader("src/main/resources/mapper/UserMapper.xml");
    }

    @Test
    public void testReadXmlForConfig(){
        Configuration properties = xmlConfigReader.getConfigProperties();
        String string = properties.toString();
        System.out.println(string);

    }

    @Test
    public void testReadXmlForMapper(){
        MapperConfiguration mapperProperties = xmlMapperReader.getMapperProperties();
        System.out.println(mapperProperties.toString());
    }

    @Test
    public void testReadXml(){
        Configuration configuration = Configuration.getConfiguration("src/main/resources/mybatis-config.xml");
        System.out.println(configuration);
    }



}
