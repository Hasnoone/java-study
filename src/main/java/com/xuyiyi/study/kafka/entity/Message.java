package com.xuyiyi.study.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:44
 */
@Data
@AllArgsConstructor
public class Message {

    private int id;

    private String msg;


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                '}';
    }
}
