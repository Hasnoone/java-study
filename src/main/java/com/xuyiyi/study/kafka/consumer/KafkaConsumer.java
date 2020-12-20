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
//    @KafkaListener(id = "myGroup",topics = {"abc123"},containerFactory = "batchFactory")
//    public void consumerListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("消费消息" + Thread.currentThread().getName() + ":" + record.value());

//        records.forEach(record -> {
//            String value = record.value();
//            log.info("消费消息" + Thread.currentThread().getName() + ":" + value);
//        });
//        ack.acknowledge();//启用这句话的话,就会开启手动提交
    }



}
