package com.xuyiyi.study;

import com.xuyiyi.study.kafka.producer.KafkaConsumer;
import com.xuyiyi.study.kafka.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class StudyApplicationTests {


    @Autowired
    KafkaConsumer kafkaConsumer;

    @Autowired
    KafkaProducer kafkaProducer;

    @Test
    void TestKafka() {
        kafkaProducer.sendMessage();
    }

}
