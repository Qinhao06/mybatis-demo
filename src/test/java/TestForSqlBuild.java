import com.example.model.User;
import com.example.mapper.UserMapper;
import org.example.build.SqlBuilder;
import org.example.config.Configuration;
import org.example.config.MapperConfiguration;
import org.junit.Test;

public class TestForSqlBuild {

    @Test
    public void testSqlBuild() throws NoSuchMethodException {
        Configuration configuration = Configuration.getConfiguration();
        System.out.println(configuration);
        MapperConfiguration mapperConfiguration = configuration.getMappers().get("com.example.mapper.UserMapper");
        System.out.println(mapperConfiguration);
        org.example.build.SqlEntity sqlEntity = mapperConfiguration.getSqlStatements().get("insertUser");
        String string = SqlBuilder.buildSql(sqlEntity, new Object[]{new User("1", "1")},
                UserMapper.class.getMethod("insertUser", User.class));
        System.out.println(string);
        sqlEntity = mapperConfiguration.getSqlStatements().get("selectUserById");
        string = SqlBuilder.buildSql(sqlEntity, new Object[]{1},
                UserMapper.class.getMethod("selectUserById", int.class));
        System.out.println(string);
    }

}
