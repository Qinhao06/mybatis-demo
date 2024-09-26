import com.example.mapper.UserMapper;
import com.example.model.User;
import org.example.config.Configuration;
import org.example.connection.JDBCConnectionPoolManager;
import org.example.connection.JDBCSimpleConnectionManager;
import org.example.proxy.MapperProxyFactory;
import org.junit.Test;

import java.sql.SQLException;

public class TestForProxy {

    @Test
    public void testConnectionExecute() {
        JDBCSimpleConnectionManager jdbcSimpleConnectionManager = new JDBCSimpleConnectionManager();
        MapperProxyFactory mapperProxyFactory =
                new MapperProxyFactory(Configuration.getConfiguration(), jdbcSimpleConnectionManager);
        UserMapper mapperProxy = (UserMapper)mapperProxyFactory.getMapperProxy(UserMapper.class);
        mapperProxy.insertUser(new User( "test", "test@test.com"));
        mapperProxy.updateUser(new User("test2", "test2@test.com"));
        mapperProxy.deleteUser(1);
        System.out.println(mapperProxy.selectUserById(1));
        System.out.println(mapperProxy.selectAllUsers());
    }

    @Test
    public void testConnectionPoolExecute() throws SQLException {
        JDBCConnectionPoolManager jdbcSimpleConnectionManager = new JDBCConnectionPoolManager(5, 2);
        MapperProxyFactory mapperProxyFactory =
                new MapperProxyFactory(Configuration.getConfiguration(), jdbcSimpleConnectionManager);
        for (int i = 40; i < 60; i++) {
            int finalI = i;
            new Thread(() -> {
                    UserMapper mapperProxy = (UserMapper)mapperProxyFactory.getMapperProxy(UserMapper.class);
                    mapperProxy.insertUser(new User( "test1" + finalI, "test@test.com" + finalI));
                    User users = mapperProxy.selectUserById(10);
                System.out.println(users);
            }).start();
        }
    }

    public static void main(String[] args) {
        JDBCConnectionPoolManager jdbcSimpleConnectionManager = new JDBCConnectionPoolManager(5, 2);
        JDBCSimpleConnectionManager jdbcSimpleConnectionManager2 = new JDBCSimpleConnectionManager();
        MapperProxyFactory mapperProxyFactory =
                new MapperProxyFactory(Configuration.getConfiguration(), jdbcSimpleConnectionManager);
        for (int i = 40; i < 60; i++) {
            new Thread(() -> {
                UserMapper mapperProxy = (UserMapper)mapperProxyFactory.getMapperProxy(UserMapper.class);
                User users = mapperProxy.selectUserById(10);
                System.out.println( users);

            }).start();
        }
    }
}
