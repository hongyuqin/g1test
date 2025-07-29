#!/bin/bash
set -e

# 等待MySQL主服务器启动
echo "等待MySQL主服务器启动..."
until mysqladmin ping -h mysql-master -u root -proot --silent; do
    sleep 3
    echo "等待MySQL主服务器..."
done
echo "MySQL主服务器已启动"

# 等待MySQL从服务器启动
echo "等待MySQL从服务器启动..."
until mysqladmin ping -h mysql-slave -u root -proot --silent; do
    sleep 3
    echo "等待MySQL从服务器..."
done
echo "MySQL从服务器已启动"

# 确保主库有复制用户
echo "确保主库有复制用户..."
mysql -h mysql-master -u root -proot -e "
DROP USER IF EXISTS 'repl'@'%';
CREATE USER 'repl'@'%' IDENTIFIED WITH mysql_native_password BY 'repl_password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;"

# 确保主库有测试数据
echo "确保主库有测试数据..."
mysql -h mysql-master -u root -proot -e "
USE testdb;
CREATE TABLE IF NOT EXISTS test_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO test_table (name, description) VALUES 
('测试数据1', '这是主从复制测试数据1'),
('测试数据2', '这是主从复制测试数据2');
COMMIT;"

# 确保从库有相同的初始数据
echo "确保从库有相同的初始数据..."
mysql -h mysql-slave -u root -proot -e "
USE testdb;
CREATE TABLE IF NOT EXISTS test_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO test_table (name, description) VALUES 
('测试数据1', '这是主从复制测试数据1'),
('测试数据2', '这是主从复制测试数据2');
COMMIT;"

# 获取主服务器的二进制日志信息
echo "获取主服务器的二进制日志信息..."
MASTER_STATUS=$(mysql -h mysql-master -u root -proot -e "SHOW MASTER STATUS\G")
MASTER_LOG_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
MASTER_LOG_POS=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')

echo "主服务器日志文件: $MASTER_LOG_FILE"
echo "主服务器日志位置: $MASTER_LOG_POS"

# 停止从库复制（如果正在运行）
echo "停止从库复制..."
mysql -h mysql-slave -u root -proot -e "STOP SLAVE;" || true

# 重置从库复制状态
echo "重置从库复制状态..."
mysql -h mysql-slave -u root -proot -e "RESET SLAVE;" || true

# 配置从服务器
echo "配置从服务器..."
mysql -h mysql-slave -u root -proot -e "
CHANGE MASTER TO 
MASTER_HOST='mysql-master', 
MASTER_USER='repl', 
MASTER_PASSWORD='repl_password', 
MASTER_LOG_FILE='$MASTER_LOG_FILE', 
MASTER_LOG_POS=$MASTER_LOG_POS;
START SLAVE;"

# 等待复制启动
echo "等待复制启动..."
sleep 5

# 检查从服务器状态
echo "检查从服务器状态..."
SLAVE_STATUS=$(mysql -h mysql-slave -u root -proot -e "SHOW SLAVE STATUS\G")
IO_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
SQL_RUNNING=$(echo "$SLAVE_STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')

if [ "$IO_RUNNING" == "Yes" ] && [ "$SQL_RUNNING" == "Yes" ]; then
    echo "主从复制配置成功！"
    echo "Slave_IO_Running: $IO_RUNNING"
    echo "Slave_SQL_Running: $SQL_RUNNING"
else
    echo "主从复制配置失败！"
    echo "Slave_IO_Running: $IO_RUNNING"
    echo "Slave_SQL_Running: $SQL_RUNNING"
    echo "完整的从服务器状态:"
    echo "$SLAVE_STATUS"
    exit 1
fi

# 等待复制完成
echo "等待复制完成..."
sleep 10

# 验证复制
echo "验证复制..."
mysql -h mysql-master -u root -proot -e "USE testdb; INSERT INTO test_table (name, description) VALUES ('验证数据', '这是验证主从复制的数据');"
sleep 5
MASTER_COUNT=$(mysql -h mysql-master -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" | tail -1)
SLAVE_COUNT=$(mysql -h mysql-slave -u root -proot -e "SELECT COUNT(*) FROM testdb.test_table;" | tail -1)

echo "主服务器记录数: $MASTER_COUNT"
echo "从服务器记录数: $SLAVE_COUNT"

if [ "$MASTER_COUNT" == "$SLAVE_COUNT" ]; then
    echo "验证成功！主从数据一致。"
else
    echo "验证失败！主从数据不一致。"
    echo "主库数据:"
    mysql -h mysql-master -u root -proot -e "SELECT * FROM testdb.test_table;"
    echo "从库数据:"
    mysql -h mysql-slave -u root -proot -e "SELECT * FROM testdb.test_table;"
    exit 1
fi

echo "MySQL主从复制设置完成！" 