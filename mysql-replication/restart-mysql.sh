#!/bin/bash

echo "=== 重新启动MySQL主从复制环境 ==="

# 停止并删除现有容器
echo "1. 停止并删除现有容器..."
docker-compose -f docker-compose-mysql.yml down -v

# 清理可能存在的旧数据
echo "2. 清理旧数据..."
docker volume prune -f

# 重新启动环境
echo "3. 重新启动环境..."
docker-compose -f docker-compose-mysql.yml up -d

# 等待容器启动
echo "4. 等待容器启动..."
sleep 30

# 查看设置日志
echo "5. 查看设置日志..."
docker-compose -f docker-compose-mysql.yml logs mysql-setup

# 运行快速测试
echo "6. 运行快速测试..."
./quick-test.sh

echo "=== 重新启动完成 ===" 