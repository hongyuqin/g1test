#!/bin/bash

echo "=== 动态数据源功能测试 ==="

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
echo "2. 测试业务接口（读主库）..."
BUSINESS_RESPONSE=$(curl -s http://localhost:8081/api/users/business)
echo "响应: $BUSINESS_RESPONSE"

echo ""
echo "3. 测试管理后台接口（读从库）..."
ADMIN_RESPONSE=$(curl -s http://localhost:8081/api/users/admin)
echo "响应: $ADMIN_RESPONSE"

echo ""
echo "4. 测试添加用户（写主库）..."
ADD_RESPONSE=$(curl -s -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"测试用户","description":"这是一个测试用户"}')
echo "响应: $ADD_RESPONSE"

echo ""
echo "5. 测试数据对比..."
COMPARE_RESPONSE=$(curl -s http://localhost:8081/api/users/compare)
echo "响应: $COMPARE_RESPONSE"

echo ""
echo "=== 测试完成 ==="
echo ""
echo "接口说明:"
echo "- /api/users/business  - 业务接口（读主库）"
echo "- /api/users/admin     - 管理后台接口（读从库）"
echo "- /api/users           - 添加用户（写主库）"
echo "- /api/users/compare   - 数据对比" 