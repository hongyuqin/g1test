# Sharding-JDBC 读写分离功能说明

## 功能概述

g1test项目已集成Sharding-JDBC读写分离功能，实现：
- **自动读写分离**：无需手动注解，根据SQL类型自动路由
- **管理后台读从库**：查询操作自动路由到从库
- **主业务写主库**：写操作自动路由到主库
- **事务自动处理**：事务操作自动路由到主库

## 技术栈

- Spring Boot 2.6.14
- MyBatis-Plus 3.5.3.1
- Sharding-JDBC 5.3.2
- HikariCP 连接池

## 核心特性

### 1. 自动读写分离
```java
// 无需手动注解，Sharding-JDBC自动识别SQL类型
public List<User> getUsers() {
    return this.list(); // SELECT语句自动路由到从库
}

public boolean addUser(User user) {
    return this.save(user); // INSERT语句自动路由到主库
}
```

### 2. 事务自动处理
```java
@Transactional
public void createOrder() {
    // 事务中的所有操作自动路由到主库
    orderMapper.insert(order);
    stockMapper.update(stock);
}
```

## 使用方法

### 1. 启动MySQL主从环境
```bash
cd mysql-replication
docker-compose -f docker-compose-mysql.yml up -d
```

### 2. 启动应用
```bash
mvn spring-boot:run
```

### 3. 测试接口

#### 业务接口（Sharding-JDBC自动路由到主库）
```bash
curl http://localhost:8081/api/users/business
```

#### 管理后台接口（Sharding-JDBC自动路由到从库）
```bash
curl http://localhost:8081/api/users/admin
```

#### 添加用户（Sharding-JDBC自动路由到主库）
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"测试用户","description":"这是一个测试用户"}'
```

#### 事务测试（Sharding-JDBC自动路由到主库）
```bash
curl -X POST http://localhost:8081/api/users/transaction \
  -H "Content-Type: application/json" \
  -d '{"name":"事务用户","description":"这是事务测试用户"}'
```

#### 数据对比
```bash
curl http://localhost:8081/api/users/compare
```

## 配置说明

### Sharding-JDBC配置
```yaml
spring:
  shardingsphere:
    datasource:
      names: master,slave
      master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:13306/testdb
        username: root
        password: root
      slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:13307/testdb
        username: root
        password: root
    
    rules:
      readwrite-splitting:
        data-sources:
          readwrite_ds:
            write-data-source-name: master
            read-data-source-names: slave
            load-balancer-name: round_robin
        load-balancers:
          round_robin:
            type: ROUND_ROBIN
```

### 连接池配置
- 最小空闲连接：5
- 最大连接池大小：20
- 连接超时：30秒
- 空闲超时：10分钟
- 最大生命周期：30分钟

## 与动态数据源对比

| 特性 | 动态数据源 | Sharding-JDBC |
|------|------------|---------------|
| **配置复杂度** | 高（需要注解） | 低（自动识别） |
| **代码侵入性** | 高（需要注解） | 低（无侵入） |
| **功能完整性** | 基础读写分离 | 分库分表+读写分离 |
| **事务支持** | 需要手动处理 | 自动事务路由 |
| **扩展性** | 有限 | 强（支持分库分表） |
| **学习成本** | 中等 | 低 |

## 优势

1. **零侵入**：无需修改业务代码，自动识别SQL类型
2. **自动路由**：根据SQL类型自动选择主库或从库
3. **事务安全**：事务操作自动路由到主库，保证一致性
4. **扩展性强**：支持分库分表、分布式事务等高级功能
5. **配置简单**：通过YAML配置即可实现读写分离

## 注意事项

1. 确保主从复制正常工作
2. 从库可能存在数据延迟
3. 写操作和事务操作自动路由到主库
4. 支持未来扩展分库分表功能

## 监控和调试

### 开启SQL日志
```yaml
spring:
  shardingsphere:
    props:
      sql-show: true
      sql-simple: true
```

### 查看路由日志
应用启动后，可以在日志中看到Sharding-JDBC的路由信息：
```
ShardingSphere-SQL - Logic SQL: SELECT * FROM test_table
ShardingSphere-SQL - Actual SQL: slave ::: SELECT * FROM test_table
``` 