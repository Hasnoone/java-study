package com.xuyiyi.study.web.controller;


import com.xuyiyi.study.entity.Message;
import com.xuyiyi.study.web.controller.api.KafkaApi;
import com.xuyiyi.study.web.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KafkaController implements KafkaApi {


    private final KafkaService kafkaService;

    @Autowired
    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;

    }

    @Override
    public String sentMsg(@RequestBody Message msg) {
        return kafkaService.sentMsg(msg);
    }
}
