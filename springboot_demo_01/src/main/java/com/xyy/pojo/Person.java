package com.xyy.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "person")
//@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
@Data
@Component//这个注解作用就是在包扫描时，将当前类实例存储到ioc容器当中
public class Person {
    private Integer id;
    private String name;
    private List hobby;
    private String[] family;
    private Map map;
    private Pet pet;
}
