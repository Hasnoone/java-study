package com.xuyiyi.study.web.service;

import com.xuyiyi.study.entity.Message;

import java.util.List;

public interface KafkaService {
    String sentMsg(Message msg);

    String createTopic(String topicName);

    List<String> listAllTopic();
}
