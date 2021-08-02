package com.xyy.service;

import com.xyy.pojo.AnotherComponent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyService {
    @Bean
    @ConfigurationProperties(prefix = "another")
    public AnotherComponent getAnotherComponent() {
        return new AnotherComponent();
    }

}
