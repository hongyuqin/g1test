package com.huf.g1test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class ConfigChangeListener {
    @EventListener(EnvironmentChangeEvent.class) // 监听环境变更事件
    public void handleRefresh(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        log.info("handleRefresh xxx");
        // 更新本地缓存或执行业务逻辑
    }
}