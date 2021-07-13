package com.xuyiyi.shouxie.demo.controller;


import com.xuyiyi.shouxie.demo.service.DemoService;
import com.xuyiyi.shouxie.mvcframework.annotation.MyAutowired;
import com.xuyiyi.shouxie.mvcframework.annotation.MyController;
import com.xuyiyi.shouxie.mvcframework.annotation.MyRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("/demo")
public class DemoController {


    @MyAutowired
    private DemoService demoService;


    @MyRequestMapping(value = "/test")
    public void test(HttpServletRequest request, HttpServletResponse response, String name) {
        demoService.test(name);
        System.out.println("hhhhhhhhh");

    }





}
