package com.xuyiyi.study.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:45
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(id = "myGroup",topics = {"abc123"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("record =" + record);
            log.info("message =" + message);
        }
    }




}
