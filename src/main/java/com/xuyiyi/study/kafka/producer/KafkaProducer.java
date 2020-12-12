package com.xuyiyi.study.kafka.producer;

import com.xuyiyi.study.kafka.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:45
 */
@Component
@Slf4j
public class KafkaProducer {

    public static final String topic = "abc123";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessageSync(Message message) throws InterruptedException, ExecutionException, TimeoutException {
        kafkaTemplate.send(topic, message.toString()).get(10, TimeUnit.SECONDS);
    }





    /**
     * 同步推送
     */
    public void sendMessageAsync(Message message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message.toString());

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info(" - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                log.info( " - 生产者 发送消息成功：" + stringStringSendResult.toString());
            }
        });

    }

}
