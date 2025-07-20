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

-- 插入测试数据
INSERT INTO `account` (`user_id`, `balance`, `currency`, `status`, `create_time`, `update_time`) 
VALUES ('user001', 1000.00, 'CNY', 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE `balance` = 1000.00, `update_time` = NOW();

INSERT INTO `account` (`user_id`, `balance`, `currency`, `status`, `create_time`, `update_time`) 
VALUES ('user002', 500.00, 'CNY', 'ACTIVE', NOW(), NOW())
ON DUPLICATE KEY UPDATE `balance` = 500.00, `update_time` = NOW(); 