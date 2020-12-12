package com.xuyiyi.study.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daguji
 * @Description
 * @create 2020-12-01 22:00
 */
@Configuration
public class KafkaTopicConfig {


    /**
     * 定义一个KafkaAdmin的bean，可以自动检测集群中是否存在topic，不存在则创建
     * @return
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        // 指定多个kafka集群多个地址，例如：192.168.2.11,9092,192.168.2.12:9092,192.168.2.13:9092
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"39.101.203.179:9092");
        return new KafkaAdmin(configs);

    }


    @Bean
    public NewTopic newTopic() {
        return new NewTopic("test", 3, (short) 0);
    }


}
