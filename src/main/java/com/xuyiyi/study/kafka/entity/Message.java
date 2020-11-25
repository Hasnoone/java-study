package com.xuyiyi.study.kafka.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Daguji
 * @Description
 * @create 2020-11-25 21:44
 */
@Data
public class Message {


    private Long id;

    private String msg;

    private Date sendTime;


}
