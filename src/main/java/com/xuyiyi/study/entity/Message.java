package com.xuyiyi.study.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

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
