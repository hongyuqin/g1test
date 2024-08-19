package com.huf.g1test.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class BasicController {
    @Timed("basic.hello")
    @RequestMapping("hello")
    public Map hello(){
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (Exception e) {
            throw new RuntimeException();
        }
        Map result = new HashMap();
        result.put("code", "200");
        result.put("message", "hello");
        return result;
    }
}
