#!/bin/bash

echo "=== MySQL主从库请求监控 ==="
echo "按 Ctrl+C 停止监控"
echo ""

# 监控主库请求
echo "🔍 监控主库请求..."
docker exec mysql-master mysql -u root -proot -e "
SET GLOBAL general_log = 'ON';
SET GLOBAL log_output = 'TABLE';
" > /dev/null 2>&1

# 监控从库请求
echo "🔍 监控从库请求..."
docker exec mysql-slave mysql -u root -proot -e "
SET GLOBAL general_log = 'ON';
SET GLOBAL log_output = 'TABLE';
" > /dev/null 2>&1

echo "✅ 通用日志已开启，现在可以监控请求了"
echo ""

# 实时查看主库日志
echo "📊 主库请求日志："
docker exec mysql-master mysql -u root -proot -e "
SELECT 
    event_time,
    user_host,
    thread_id,
    server_id,
    command_type,
    argument
FROM mysql.general_log 
WHERE command_type = 'Query' 
ORDER BY event_time DESC 
LIMIT 10;
"

echo ""
echo "📊 从库请求日志："
docker exec mysql-slave mysql -u root -proot -e "
SELECT 
    event_time,
    user_host,
    thread_id,
    server_id,
    command_type,
    argument
FROM mysql.general_log 
WHERE command_type = 'Query' 
ORDER BY event_time DESC 
LIMIT 10;
"

echo ""
echo "💡 提示："
echo "- 运行应用后，新的SQL请求会出现在上面的日志中"
echo "- 主库会收到写操作（INSERT/UPDATE/DELETE）"
echo "- 从库会收到读操作（SELECT）"
echo "- 可以通过 'docker exec -it mysql-master mysql -u root -proot' 进入主库"
echo "- 可以通过 'docker exec -it mysql-slave mysql -u root -proot' 进入从库" 