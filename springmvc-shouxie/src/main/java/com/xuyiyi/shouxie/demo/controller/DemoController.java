package com.xuyiyi.shouxie.demo.controller;


import com.xuyiyi.shouxie.demo.service.DemoService;
import com.xuyiyi.shouxie.mvcframework.annotation.MyAutowired;
import com.xuyiyi.shouxie.mvcframework.annotation.MyController;
import com.xuyiyi.shouxie.mvcframework.annotation.MyRequestMapping;

@MyController
@MyRequestMapping("/demo")
public class DemoController {


    @MyAutowired
    private DemoService demoService;


    @MyRequestMapping(value = "/test")
    public void test() {
        demoService.test();
        System.out.println("hhhhhhhhh");

    }





}
