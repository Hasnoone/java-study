package com.xyy.springboot_demo_01;

import com.xyy.config.JdbcConfiguration;
import com.xyy.pojo.AnotherComponent;
import com.xyy.pojo.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class SpringbootDemo01ApplicationTests {

    @Autowired
    Person person;

//    @Autowired
//    private JdbcConfiguration jdbcConfiguration;

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        log.info("person 就是 {}", person);
    }


    @Test
    void test1() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from account");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                log.info("name 就是 {}", name);
            }
            System.out.println();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Autowired
    private AnotherComponent anotherComponent;
    @Test
    public void test2() {
        log.info("显示结果：{}", anotherComponent);
    }


    @Test
    public void testLogging() {
        log.trace("trace···");
        log.debug("debug···");
        log.info("info···");
        log.warn("warn···");
        log.error("error···");

    }
}
