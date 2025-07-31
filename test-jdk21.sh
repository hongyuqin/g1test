#!/bin/bash

echo "=== JDK21 + Spring Boot 3 特性测试 ==="
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
echo "2. 测试虚拟线程..."
echo "调用虚拟线程接口..."
curl -s http://localhost:8081/api/jdk21/virtual-threads
echo ""
sleep 2

echo ""
echo "3. 测试模式匹配..."
echo "测试字符串模式:"
curl -s http://localhost:8081/api/jdk21/pattern-matching/string
echo ""
echo "测试数字模式:"
curl -s http://localhost:8081/api/jdk21/pattern-matching/number
echo ""
sleep 1

echo ""
echo "4. 测试记录模式..."
curl -s http://localhost:8081/api/jdk21/record-patterns
echo ""
sleep 1

echo ""
echo "5. 测试有序集合..."
curl -s http://localhost:8081/api/jdk21/sequenced-collections
echo ""
sleep 1

echo ""
echo "6. 测试未命名模式..."
curl -s http://localhost:8081/api/jdk21/unnamed-patterns
echo ""
sleep 1

echo ""
echo "7. 测试性能监控..."
curl -s http://localhost:8081/api/jdk21/performance
echo ""
sleep 1

echo ""
echo "8. 测试并发示例..."
curl -s http://localhost:8081/api/jdk21/concurrency
echo ""

echo ""
echo "=== 测试完成 ==="
echo ""
echo "💡 JDK21 主要特性:"
echo "- 虚拟线程: 轻量级线程，适合IO密集型应用"
echo "- 模式匹配: 增强的switch表达式"
echo "- 记录模式: 结构化数据解构"
echo "- 有序集合: 统一的集合API"
echo "- 未命名模式: 使用_忽略不需要的字段" 