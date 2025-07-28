package com.huf.g1test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
@RequestMapping("/config")
public class ConfigController {
    
    @Value("${name:default}")
    private String name;
    
    @Value("${order.decode.corePoolSize:0}")
    private int corePoolSize;
    
    @Value("${order.decode.maxPoolSize:0}")
    private int maxPoolSize;
    
    @Value("${order.decode.queueSize:0}")
    private int queueSize;

    
    @GetMapping("/info")
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> config = new HashMap<>();
        config.put("name", name);
        config.put("corePoolSize", corePoolSize);
        config.put("maxPoolSize", maxPoolSize);
        config.put("queueSize", queueSize);
        return config;
    }
} 