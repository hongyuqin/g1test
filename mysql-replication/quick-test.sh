#!/bin/bash

echo "=== MySQL主从复制快速测试 ==="

# 检查容器是否运行
echo "1. 检查容器状态..."
if docker ps | grep -q "mysql-master" && docker ps | grep -q "mysql-slave"; then
    echo "✅ 容器运行正常"
else
    echo "❌ 容器未运行，请先启动环境"
    exit 1
fi

# 检查从库复制状态
echo "2. 检查从库复制状态..."
SLAVE_STATUS=$(docker exec mysql-slave mysql -u root -proot -e "SHOW SLAVE STATUS\G" 2>/dev/null)
IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')

echo "Slave_IO_Running: $IO_RUNNING"
echo "Slave_SQL_Running: $SQL_RUNNING"

if [ "$IO_RUNNING" == "Yes" ] && [ "$SQL_RUNNING" == "Yes" ]; then
    echo "✅ 复制状态正常"
else
    echo "❌ 复制状态异常"
    echo "完整状态信息:"
    echo "$SLAVE_STATUS"
    exit 1
fi

# 检查数据同步
echo "3. 检查数据同步..."
MASTER_COUNT=$(docker exec mysql-master mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)
SLAVE_COUNT=$(docker exec mysql-slave mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)

echo "主库记录数: $MASTER_COUNT"
echo "从库记录数: $SLAVE_COUNT"

if [ "$MASTER_COUNT" == "$SLAVE_COUNT" ]; then
    echo "✅ 数据同步正常"
else
    echo "❌ 数据同步异常"
fi

echo "=== 快速测试完成 ===" 