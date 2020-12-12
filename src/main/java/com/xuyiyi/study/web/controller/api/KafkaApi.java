package com.xuyiyi.study.web.controller.api;


import com.xuyiyi.study.entity.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "kafkaapi")
public interface KafkaApi {


    @PostMapping("/sentMsg")
    String sentMsg(Message msg);

}
