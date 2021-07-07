package com.xuyiyi.shouxie.demo.service.impl;

import com.xuyiyi.shouxie.demo.service.DemoService;
import com.xuyiyi.shouxie.mvcframework.annotation.MyService;

@MyService
public class DemoServiceImpl implements DemoService {


    @Override
    public void test() {
        System.out.println("===============");
    }
}
