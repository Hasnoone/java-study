package com.xyy.controller;

import com.xyy.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {


    @Autowired
    private Person person;

    @RequestMapping("demo")
    public String demo() {
        System.out.println(person);
        return "hello world!";
    }


}
