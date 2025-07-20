# Seata 分布式事务 Demo

这是一个简单的 Seata 分布式事务演示项目，展示了如何使用 Seata 的 AT 模式实现分布式事务。

## 项目结构

```
src/main/java/com/huf/g1test/
├── controller/
│   └── SeataDemoController.java    # REST API 控制器
├── entity/
│   ├── Account.java                # 账户实体
│   └── Order.java                  # 订单实体
├── mapper/
│   ├── AccountMapper.java          # 账户数据访问层
│   └── OrderMapper.java            # 订单数据访问层
├── service/
│   ├── AccountService.java         # 账户服务接口
│   ├── OrderService.java           # 订单服务接口
│   ├── BusinessService.java        # 业务服务接口
│   └── impl/
│       ├── AccountServiceImpl.java  # 账户服务实现
│       ├── OrderServiceImpl.java    # 订单服务实现
│       └── BusinessServiceImpl.java # 业务服务实现
└── G1testApplication.java          # 启动类
```

## 环境准备

### 1. 启动 Seata Server

确保你已经通过 docker-compose 启动了 Seata Server，监听端口 8091。

### 2. 创建数据库

执行 `src/main/resources/sql/init.sql` 脚本创建数据库和表：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS seata_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE seata_demo;

-- 创建账户表
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `currency` varchar(10) NOT NULL DEFAULT 'CNY' COMMENT '货币类型',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '账户状态',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `status` varchar(20) NOT NULL DEFAULT 'CREATED' COMMENT '订单状态',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

### 3. 配置数据库连接

修改 `application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/seata_demo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

## 启动应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

## API 接口

### 1. 健康检查
```
GET /seata/health
```

### 2. 初始化账户
```
POST /seata/init-account?userId=user001&balance=1000
```

### 3. 查询账户
```
GET /seata/account/user001
```

### 4. 创建订单（正常流程）
```
POST /seata/create-order?userId=user001&amount=100
```

### 5. 创建订单（模拟异常）
```
POST /seata/create-order-exception?userId=user001&amount=100
```

### 6. 查询订单
```
GET /seata/order/ORDER_12345678
```

## 测试场景

### 场景1：正常流程
1. 初始化账户：`POST /seata/init-account?userId=user001&balance=1000`
2. 查询账户余额：`GET /seata/account/user001`
3. 创建订单：`POST /seata/create-order?userId=user001&amount=100`
4. 再次查询账户余额：`GET /seata/account/user001`
5. 查询订单：`GET /seata/order/{orderNo}`

**预期结果**：账户余额减少100，订单状态为"PAID"

### 场景2：异常回滚
1. 初始化账户：`POST /seata/init-account?userId=user002&balance=500`
2. 查询账户余额：`GET /seata/account/user002`
3. 创建订单（模拟异常）：`POST /seata/create-order-exception?userId=user002&amount=100`
4. 再次查询账户余额：`GET /seata/account/user002`

**预期结果**：账户余额不变，订单创建失败，事务回滚

## 核心特性

### 1. @GlobalTransactional 注解
在 `BusinessServiceImpl` 中使用 `@GlobalTransactional` 注解标记分布式事务：

```java
@GlobalTransactional(name = "create-order-deduct-balance", rollbackFor = Exception.class)
public boolean createOrderAndDeductBalance(String userId, String orderNo, BigDecimal amount) {
    // 业务逻辑
}
```

### 2. AT 模式
- 自动生成 undo log
- 自动回滚机制
- 对业务代码无侵入

### 3. 事务回滚
当业务方法抛出异常时，Seata 会自动回滚所有分支事务，保证数据一致性。

## 日志观察

启动应用后，观察控制台日志：

1. **事务开始**：`Global transaction [xxx] is beginning`
2. **分支事务注册**：`Branch register [xxx]`
3. **事务提交/回滚**：`Global transaction [xxx] is committing/rollbacking`

## 注意事项

1. 确保 Seata Server 正常运行
2. 确保数据库连接正常
3. 确保数据库表已创建
4. 观察日志了解事务执行过程

## 扩展

可以基于此 demo 扩展：
- 添加更多业务表
- 实现 TCC 模式
- 添加 SAGA 模式
- 集成注册中心（Nacos、Eureka等） 