# 主从读写分离框架对比

## 🏆 推荐方案

### 1. **Spring Boot + MyBatis-Plus + 动态数据源** ⭐⭐⭐⭐⭐
**适用场景**：Java项目，需要细粒度控制

**优势**：
- 通过注解实现透明切换
- 支持多数据源扩展
- 与Spring生态完美集成
- 性能优秀，易于维护

**实现方式**：
```java
@DataSource(DataSourceEnum.MASTER)
public List<User> getUsersFromMaster() {
    return userMapper.selectList(null);
}

@DataSource(DataSourceEnum.SLAVE)
public List<User> getUsersFromSlave() {
    return userMapper.selectList(null);
}
```

### 2. **Sharding-JDBC** ⭐⭐⭐⭐
**适用场景**：分库分表 + 读写分离

**优势**：
- 支持分库分表
- 自动读写分离
- 性能优秀
- 配置简单

**配置示例**：
```yaml
spring:
  shardingsphere:
    datasource:
      names: master,slave
    rules:
      readwrite-splitting:
        data-sources:
          readwrite_ds:
            write-data-source-name: master
            read-data-source-names: slave
```

### 3. **MyCat** ⭐⭐⭐
**适用场景**：数据库中间件方案

**优势**：
- 数据库层面的读写分离
- 支持多种数据库
- 配置灵活

**缺点**：
- 需要额外的中间件
- 运维复杂度高

## 🔧 其他可选方案

### 4. **Spring Cloud + Feign + 多服务**
**适用场景**：微服务架构

**实现方式**：
- 业务服务：连接主库
- 管理服务：连接从库
- 通过Feign进行服务间调用

### 5. **Redis + 缓存策略**
**适用场景**：读多写少的场景

**实现方式**：
- 写操作：直接写主库
- 读操作：优先读缓存，缓存未命中读从库

### 6. **数据库代理（ProxySQL）**
**适用场景**：数据库层面的读写分离

**优势**：
- 对应用透明
- 支持负载均衡
- 自动故障转移

## 📊 方案对比

| 方案 | 复杂度 | 性能 | 维护成本 | 扩展性 | 推荐指数 |
|------|--------|------|----------|--------|----------|
| Spring Boot + 动态数据源 | 中等 | 高 | 低 | 高 | ⭐⭐⭐⭐⭐ |
| Sharding-JDBC | 低 | 高 | 低 | 高 | ⭐⭐⭐⭐ |
| MyCat | 高 | 中 | 高 | 中 | ⭐⭐⭐ |
| 微服务分离 | 高 | 中 | 高 | 高 | ⭐⭐⭐ |
| Redis缓存 | 中等 | 高 | 中等 | 中等 | ⭐⭐⭐⭐ |
| ProxySQL | 中等 | 高 | 中等 | 中等 | ⭐⭐⭐ |

## 🎯 选择建议

### 小型项目
推荐：**Spring Boot + 动态数据源**
- 实现简单，维护成本低
- 满足基本读写分离需求

### 中型项目
推荐：**Sharding-JDBC**
- 功能更全面，支持分库分表
- 性能优秀，配置简单

### 大型项目
推荐：**微服务架构 + 数据库代理**
- 服务解耦，扩展性强
- 数据库层面的读写分离

## 🚀 快速开始

### 使用Spring Boot动态数据源
1. 克隆示例项目
2. 启动MySQL主从环境
3. 运行应用并测试接口

```bash
# 启动环境
cd mysql-replication
docker-compose -f docker-compose-mysql.yml up -d

# 运行示例
cd dynamic-datasource-example
mvn spring-boot:run

# 测试接口
curl http://localhost:8080/api/users/business  # 读主库
curl http://localhost:8080/api/users/admin     # 读从库
```

## ⚠️ 注意事项

1. **数据一致性**：从库可能存在延迟，需要根据业务需求选择
2. **事务处理**：跨数据源事务需要特别注意
3. **故障处理**：需要实现从库故障时的降级策略
4. **监控告警**：建议添加数据源健康检查和监控 