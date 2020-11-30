package com.xuyiyi.study.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, "test");

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println(" - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println( " - 生产者 发送消息成功：" + stringStringSendResult.toString());
            }
        });

    }




}
