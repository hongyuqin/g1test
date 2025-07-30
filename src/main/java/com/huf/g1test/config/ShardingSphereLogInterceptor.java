package com.huf.g1test.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * ShardingSphere SQL日志拦截器
 * 用于显示SQL路由到主库还是从库
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class ShardingSphereLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        
        // 根据SQL类型判断路由到主库还是从库
        String dataSourceType = determineDataSource(commandType);
        
        long startTime = System.currentTimeMillis();
        
        // 打印SQL执行前的路由信息
        log.info("【SQL路由】{} 即将路由到{} - SQL: {}", sqlId, dataSourceType, sql);
        
        // 执行SQL
        Object result = invocation.proceed();
        
        // 计算执行时间
        long endTime = System.currentTimeMillis();
        long sqlCost = endTime - startTime;
        
        // 打印SQL执行后的结果信息
        log.info("【SQL路由】{} 已路由到{} - 耗时: {}ms", sqlId, dataSourceType, sqlCost);
        
        return result;
    }

    /**
     * 根据SQL类型判断路由到主库还是从库
     */
    private String determineDataSource(SqlCommandType commandType) {
        switch (commandType) {
            case SELECT:
                return "从库";
            case INSERT:
            case UPDATE:
            case DELETE:
                return "主库";
            default:
                return "未知库";
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
} 