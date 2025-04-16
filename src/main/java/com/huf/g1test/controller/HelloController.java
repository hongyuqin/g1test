package com.huf.g1test.controller;

import com.huf.g1test.service.CService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private CService cService;

    @GetMapping("test")
    public void testTransactional(){
        cService.testTransactional();
    }
}
