package com.xuyiyi.study.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:45
 */
@Component
@Slf4j
public class KafkaConsumer {

//    @KafkaListener(id = "myGroup",topics = {"abc123"})
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("record =" + record);
            log.info("message =" + message);
        }
    }


    @KafkaListener(id = "myGroup",topics = {"abc123"},containerFactory = "kafkaCustomizeFactory")
    public void consumerListener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info(record.value());

    }



}
