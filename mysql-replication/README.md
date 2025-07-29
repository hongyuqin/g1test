# MySQL主从复制环境

## 快速开始

```bash
# 启动环境
docker-compose -f docker-compose-mysql.yml up -d

# 查看状态
docker-compose -f docker-compose-mysql.yml ps

# 测试复制
./quick-test.sh
```

## 连接信息

| 服务 | 端口 | 用户名 | 密码 |
|------|------|--------|------|
| 主库 | 13306 | root | root |
| 从库 | 13307 | root | root |

## 脚本说明

- `quick-test.sh` - 快速测试复制状态
- `test-replication.sh` - 完整测试（包含数据同步）
- `restart-mysql.sh` - 重新初始化环境

## 手动验证

```bash
# 连接主库
docker exec -it mysql-master mysql -u root -proot

# 连接从库
docker exec -it mysql-slave mysql -u root -proot

# 检查复制状态
SHOW SLAVE STATUS\G
```

## 故障排查

```bash
# 查看日志
docker-compose -f docker-compose-mysql.yml logs mysql-setup

# 重新初始化
./restart-mysql.sh
``` 