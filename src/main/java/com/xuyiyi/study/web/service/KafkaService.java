package com.xuyiyi.study.web.service;

import com.xuyiyi.study.entity.Message;

public interface KafkaService {
    String sentMsg(Message msg);
}
