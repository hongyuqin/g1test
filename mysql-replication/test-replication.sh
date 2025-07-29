#!/bin/bash

echo "=== MySQL主从复制测试脚本 ==="

# 测试主库连接
echo "1. 测试主库连接..."
if docker exec mysql-master mysql -u root -proot -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ 主库连接成功"
else
    echo "❌ 主库连接失败"
    exit 1
fi

# 测试从库连接
echo "2. 测试从库连接..."
if docker exec mysql-slave mysql -u root -proot -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ 从库连接成功"
else
    echo "❌ 从库连接失败"
    exit 1
fi

# 检查主库数据
echo "3. 检查主库数据..."
MASTER_DATA=$(docker exec mysql-master mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)
echo "主库记录数: $MASTER_DATA"

# 检查从库数据
echo "4. 检查从库数据..."
SLAVE_DATA=$(docker exec mysql-slave mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)
echo "从库记录数: $SLAVE_DATA"

# 检查从库复制状态
echo "5. 检查从库复制状态..."
SLAVE_STATUS=$(docker exec mysql-slave mysql -u root -proot -e "SHOW SLAVE STATUS\G" 2>/dev/null)
IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')

echo "Slave_IO_Running: $IO_RUNNING"
echo "Slave_SQL_Running: $SQL_RUNNING"

# 测试数据同步
echo "6. 测试数据同步..."
docker exec mysql-master mysql -u root -proot -e "USE testdb; INSERT INTO test_table (name, description) VALUES ('同步测试', '测试主从数据同步');" 2>/dev/null

sleep 3

MASTER_COUNT=$(docker exec mysql-master mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)
SLAVE_COUNT=$(docker exec mysql-slave mysql -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" 2>/dev/null | tail -1)

echo "插入后主库记录数: $MASTER_COUNT"
echo "插入后从库记录数: $SLAVE_COUNT"

if [ "$MASTER_COUNT" == "$SLAVE_COUNT" ]; then
    echo "✅ 主从复制工作正常！"
else
    echo "❌ 主从复制有问题！"
    echo "主库数据:"
    docker exec mysql-master mysql -u root -proot -e "SELECT * FROM testdb.test_table;" 2>/dev/null
    echo "从库数据:"
    docker exec mysql-slave mysql -u root -proot -e "SELECT * FROM testdb.test_table;" 2>/dev/null
fi

echo "=== 测试完成 ===" 