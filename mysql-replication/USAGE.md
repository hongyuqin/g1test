# 使用说明

## 环境要求
- Docker
- Docker Compose

## 快速使用

1. **启动环境**
   ```bash
   docker-compose -f docker-compose-mysql.yml up -d
   ```

2. **等待初始化完成**
   ```bash
   docker-compose -f docker-compose-mysql.yml logs mysql-setup
   ```

3. **测试复制**
   ```bash
   ./quick-test.sh
   ```

## 常用命令

```bash
# 查看容器状态
docker-compose -f docker-compose-mysql.yml ps

# 查看日志
docker-compose -f docker-compose-mysql.yml logs mysql-master
docker-compose -f docker-compose-mysql.yml logs mysql-slave

# 停止环境
docker-compose -f docker-compose-mysql.yml down

# 完全清理（删除数据）
docker-compose -f docker-compose-mysql.yml down -v
```

## 验证复制

在主库插入数据：
```sql
USE testdb;
INSERT INTO test_table (name, description) VALUES ('测试', '验证复制');
```

在从库查看数据：
```sql
USE testdb;
SELECT * FROM test_table;
``` 