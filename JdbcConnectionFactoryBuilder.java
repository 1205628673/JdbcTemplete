package student.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Created by cxr1205628673 on 2019/11/8.
 */
public class JdbcConnectionFactoryBuilder {
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost:3306/jlau_off?useSSL=false";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/jlau_off?useSSL=false&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    private static String USER = "root";
    private static String PASS = "root";
    private static String path = "/application.properties";
    private static DataSource dataSource;
    private static JdbcConnectionFactory factory;
    private static Properties properties = new Properties();
    public static JdbcConnectionFactory build(){
        try {
            //InputStream in = new FileInputStream(new File(path));
            InputStream i = new JdbcConnectionFactoryBuilder().getClass().getResourceAsStream(path);
            properties.load(i);
            String driver = properties.getProperty("database.driver");
            String url = properties.getProperty("database.url");
            String username = properties.getProperty("database.username");
            String password = properties.getProperty("database.password");
            System.out.print(driver);
            Class.forName(driver);
            DruidDataSource druidDataSource =(DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            druidDataSource.setDriverClassName(driver);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            dataSource = druidDataSource;
        }catch (Exception e){
            if(e instanceof SQLException) {
                e.printStackTrace();
                return null;
            }
            if(e instanceof FileNotFoundException) {
                try {
                    Class.forName(JDBC_DRIVER);
                    DruidDataSource druidDataSource = new DruidDataSource();
                    druidDataSource.setDriverClassName(JDBC_DRIVER);
                    druidDataSource.setUrl(DB_URL);
                    druidDataSource.setUsername(USER);
                    druidDataSource.setPassword(PASS);
                    dataSource = druidDataSource;
                }catch (Exception ee){
                    ee.printStackTrace();
                    return null;
                }
            }else{
                e.printStackTrace();
            }
        }
        JdbcConnectionFactory factory = new JdbcConnectionFactory(dataSource);
        return factory;
    }

    public static JdbcConnectionFactory build(DataSource datasource){
        return new JdbcConnectionFactory(datasource);
    }

    public static String getPath() {
        return path;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        JdbcConnectionFactoryBuilder.dataSource = dataSource;
    }

    public static void setPath(String path) {
        JdbcConnectionFactoryBuilder.path = path;
    }
}
