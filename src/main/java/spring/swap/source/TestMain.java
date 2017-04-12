package spring.swap.source;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestMain {
    public static void main(String[] args) throws SQLException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        for (int i = 1; i < 11; i++) {
            try {
                dataSource.getConnection();
            } catch (Exception e) {

            }
        }
    }
}
