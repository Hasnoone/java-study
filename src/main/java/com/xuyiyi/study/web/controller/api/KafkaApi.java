package com.xuyiyi.study.web.controller.api;


import com.xuyiyi.study.entity.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "kafkaapi")
public interface KafkaApi {


    @PostMapping("/sentMsg")
    String sentMsg(Message msg);

    @PostMapping("/createTopic")
    String createTopic(String topicName);

    @PostMapping("/listAllTopic")
    List<String> listAllTopic();


}
