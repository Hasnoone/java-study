package com.xuyiyi.study.web.service.impl;

import com.xuyiyi.study.entity.Message;
import com.xuyiyi.study.kafka.producer.KafkaProducer;
import com.xuyiyi.study.web.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    KafkaProducer kafkaProducer;


    @Override
    public String sentMsg(Message msg) {
        log.info(msg.toString());
        kafkaProducer.sendMessageAsync(msg);
        return "sent success";
    }
}
