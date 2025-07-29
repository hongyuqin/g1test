# 动态数据源功能说明

## 功能概述

g1test项目已集成动态数据源功能，实现：
- 管理后台读从库
- 主业务读主库
- 写操作使用主库

## 技术栈

- Spring Boot 2.6.14
- MyBatis-Plus 3.5.3.1
- Druid 连接池
- AOP 切面编程

## 核心组件

### 1. 数据源配置
- `DataSourceConfig.java` - 动态数据源配置
- `DynamicDataSource.java` - 动态数据源实现
- `DynamicDataSourceContextHolder.java` - 数据源上下文持有者

### 2. 注解和切面
- `@DataSource` - 数据源切换注解
- `DataSourceAspect.java` - AOP切面实现

### 3. 业务层
- `UserService` - 用户服务接口
- `UserServiceImpl` - 用户服务实现

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

#### 业务接口（读主库）
```bash
curl http://localhost:8081/api/users/business
```

#### 管理后台接口（读从库）
```bash
curl http://localhost:8081/api/users/admin
```

#### 添加用户（写主库）
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"测试用户","description":"这是一个测试用户"}'
```

#### 数据对比
```bash
curl http://localhost:8081/api/users/compare
```

## 注解使用

```java
// 指定使用主库
@DataSource(DataSourceEnum.MASTER)
public List<User> getUsersFromMaster() {
    return this.list();
}

// 指定使用从库
@DataSource(DataSourceEnum.SLAVE)
public List<User> getUsersFromSlave() {
    return this.list();
}
```

## 配置说明

### 数据源配置
```yaml
spring:
  datasource:
    master:
      url: jdbc:mysql://localhost:13306/testdb
      username: root
      password: root
    slave:
      url: jdbc:mysql://localhost:13307/testdb
      username: root
      password: root
```

### 连接池配置
- 初始连接数：5
- 最小空闲连接：5
- 最大活跃连接：20
- 连接超时：60秒

## 优势

1. **透明切换**：通过注解实现数据源透明切换
2. **性能优化**：读操作分散到从库，减轻主库压力
3. **高可用**：主从分离，提高系统可用性
4. **易于扩展**：可以轻松添加更多数据源

## 注意事项

1. 确保主从复制正常工作
2. 从库可能存在数据延迟
3. 写操作必须使用主库
4. 事务操作需要特别注意数据源一致性 