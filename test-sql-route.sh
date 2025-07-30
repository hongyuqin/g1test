#!/bin/bash

echo "=== 测试ShardingSphere SQL路由 ==="
echo "测试SQL路由到主库还是从库"
echo ""

# 检查应用是否运行
echo "1. 检查应用状态..."
if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
    echo "✅ 应用运行正常"
else
    echo "❌ 应用未运行，请先启动应用"
    echo "启动命令: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "2. 执行读操作（应路由到从库）..."
echo "调用查询接口..."
curl -s http://localhost:8081/api/users/admin | grep -v "data"
sleep 2

echo ""
echo "3. 执行写操作（应路由到主库）..."
echo "添加新用户..."
curl -s -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"测试用户-'$(date +%s)'","description":"测试ShardingSphere路由"}' | grep -v "data"
sleep 2

echo ""
echo "4. 执行事务操作（应路由到主库）..."
echo "事务中添加用户..."
curl -s -X POST http://localhost:8081/api/users/transaction \
  -H "Content-Type: application/json" \
  -d '{"name":"事务用户-'$(date +%s)'","description":"测试ShardingSphere事务路由"}' | grep -v "data"
sleep 2

echo ""
echo "5. 执行数据对比操作..."
curl -s http://localhost:8081/api/users/compare | grep -v "data"

echo ""
echo "=== 测试完成 ==="
echo ""
echo "💡 查看日志中的SQL路由信息："
echo "tail -f logs/g1test.log | grep '【SQL路由】'"
echo ""
echo "预期结果："
echo "- SELECT语句应路由到从库"
echo "- INSERT/UPDATE/DELETE语句应路由到主库"
echo "- 事务操作应路由到主库" 