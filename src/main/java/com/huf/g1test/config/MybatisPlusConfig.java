package com.huf.g1test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 注册ShardingSphere SQL路由日志拦截器
     */
    @Bean
    public ShardingSphereLogInterceptor shardingSphereLogInterceptor() {
        return new ShardingSphereLogInterceptor();
    }
} 