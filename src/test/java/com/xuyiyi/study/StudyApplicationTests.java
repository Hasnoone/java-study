package com.xuyiyi.study;

import com.xuyiyi.study.entity.Message;
import com.xuyiyi.study.kafka.consumer.KafkaConsumer;
import com.xuyiyi.study.kafka.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
class StudyApplicationTests {


    @Autowired
    KafkaConsumer kafkaConsumer;

    @Autowired
    KafkaProducer kafkaProducer;

    @Test
    void TestKafka() throws InterruptedException, ExecutionException, TimeoutException {
        for (int i = 0; i < 3; i++) {
            Message message = new Message(i, "测试消息");
            kafkaProducer.sendMessageAsync(message);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
