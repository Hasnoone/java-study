package com.xuyiyi.study.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:45
 */
@Component
public class KafkaProducer {

    public static final String topic = "abc123";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage() {
        kafkaTemplate.send(topic, "test");
    }




}
