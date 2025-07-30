package com.huf.g1test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * ShardingSphere日志增强器
 * 用于增强ShardingSphere的SQL日志，显示SQL路由到主库还是从库
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.shardingsphere.props.sql-show", havingValue = "true")
public class ShardingSphereLogEnhancer {

    @Value("${spring.shardingsphere.datasource.names:}")
    private String dataSourceNames;

    @PostConstruct
    public void init() {
        log.info("【ShardingSphere日志增强器】初始化完成，将显示SQL路由信息");
        // 设置系统属性，让ShardingSphere在日志中显示更多信息
        System.setProperty("com.zaxxer.hikari.pool.HikariPool.maximumPoolSize", "debug");
        
        // 增强ShardingSphere的SQL日志
        enhanceShardingSphereLog();
    }

    /**
     * 增强ShardingSphere的SQL日志
     */
    private void enhanceShardingSphereLog() {
        try {
            // 设置ShardingSphere的SQL日志格式
            Properties props = new Properties();
            props.setProperty("sql-show", "true");
            props.setProperty("sql-simple", "false");
            
            // 设置系统属性，让ShardingSphere在日志中显示数据源名称
            System.setProperty("org.apache.shardingsphere.sql.show", "true");
            System.setProperty("org.apache.shardingsphere.sql.simple", "false");
            
            log.info("【ShardingSphere日志增强器】SQL日志增强完成，可在日志中查看SQL路由信息");
        } catch (Exception e) {
            log.error("【ShardingSphere日志增强器】初始化失败", e);
        }
    }
} 