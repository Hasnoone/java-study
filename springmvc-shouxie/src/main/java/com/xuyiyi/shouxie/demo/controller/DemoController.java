package com.xuyiyi.shouxie.demo.controller;


import com.xuyiyi.shouxie.demo.service.DemoService;
import com.xuyiyi.shouxie.mvcframework.annotation.MyAutowired;
import com.xuyiyi.shouxie.mvcframework.annotation.MyController;
import com.xuyiyi.shouxie.mvcframework.annotation.MyRequestMapping;
import com.xuyiyi.shouxie.mvcframework.annotation.MySecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("/demo")
public class DemoController {


    @MyAutowired
    private DemoService demoService;


    @MyRequestMapping(value = "/test1")
    @MySecurity(value = {"wangsan","zhaoer"})
    public void test1(HttpServletRequest request, HttpServletResponse response, String name) {
        demoService.test(name);
    }


    @MyRequestMapping(value = "/test2")
    @MySecurity(value = {"liwu","liusan"})
    public void test2(HttpServletRequest request, HttpServletResponse response, String name) {
        demoService.test(name);
    }




}
