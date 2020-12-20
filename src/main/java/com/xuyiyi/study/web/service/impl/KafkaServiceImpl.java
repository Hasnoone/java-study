package com.xuyiyi.study.web.service.impl;

import com.xuyiyi.study.entity.Message;
import com.xuyiyi.study.kafka.producer.KafkaProducer;
import com.xuyiyi.study.web.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired // adminClien需要自己生成配置bean
    private AdminClient adminClient;


    @Override
    public String sentMsg(Message msg) {
        log.info(msg.toString());
        kafkaProducer.sendMessageAsync(msg);
        return "sent success";
    }

    @Override
    public String createTopic(String topicName) {
        NewTopic newTopic = new NewTopic(topicName, 10, (short) 1);
        try {
            CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));
        } catch (Exception e) {
            e.printStackTrace();
            return "false ! ";
        }
        return "success ! ";
    }

    @Override
    public List<String> listAllTopic() {
        List<String> topicNames = new ArrayList<>();
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        try {
            Set<String> strings = listTopicsResult.names().get();
            topicNames = new ArrayList<>(strings);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return topicNames;
    }
}
