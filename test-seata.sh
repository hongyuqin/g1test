#!/bin/bash

# Seata Demo 测试脚本
BASE_URL="http://localhost:8080/seata"

echo "=== Seata 分布式事务 Demo 测试 ==="
echo

# 1. 健康检查
echo "1. 健康检查..."
curl -s "$BASE_URL/health" | jq .
echo

# 2. 初始化账户
echo "2. 初始化账户 user001..."
curl -s -X POST "$BASE_URL/init-account?userId=user001&balance=1000" | jq .
echo

# 3. 查询账户
echo "3. 查询账户 user001..."
curl -s "$BASE_URL/account/user001" | jq .
echo

# 4. 创建订单（正常流程）
echo "4. 创建订单（正常流程）..."
ORDER_RESPONSE=$(curl -s -X POST "$BASE_URL/create-order?userId=user001&amount=100")
echo $ORDER_RESPONSE | jq .
ORDER_NO=$(echo $ORDER_RESPONSE | jq -r '.orderNo')
echo

# 5. 查询账户（扣减后）
echo "5. 查询账户 user001（扣减后）..."
curl -s "$BASE_URL/account/user001" | jq .
echo

# 6. 查询订单
echo "6. 查询订单..."
curl -s "$BASE_URL/order/$ORDER_NO" | jq .
echo

# 7. 初始化账户 user002
echo "7. 初始化账户 user002..."
curl -s -X POST "$BASE_URL/init-account?userId=user002&balance=500" | jq .
echo

# 8. 查询账户 user002
echo "8. 查询账户 user002..."
curl -s "$BASE_URL/account/user002" | jq .
echo

# 9. 创建订单（模拟异常）
echo "9. 创建订单（模拟异常）..."
curl -s -X POST "$BASE_URL/create-order-exception?userId=user002&amount=100" | jq .
echo

# 10. 查询账户 user002（应该余额不变）
echo "10. 查询账户 user002（应该余额不变）..."
curl -s "$BASE_URL/account/user002" | jq .
echo

echo "=== 测试完成 ==="
echo
echo "说明："
echo "- 场景1：正常流程 - 账户余额应该减少100，订单状态为PAID"
echo "- 场景2：异常回滚 - 账户余额应该不变，订单创建失败"
echo
echo "观察日志了解 Seata 事务执行过程："
echo "- 事务开始：Global transaction [xxx] is beginning"
echo "- 分支事务注册：Branch register [xxx]"
echo "- 事务提交/回滚：Global transaction [xxx] is committing/rollbacking" 