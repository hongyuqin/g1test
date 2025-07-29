-- 创建测试表（与主库保持一致）
USE testdb;
CREATE TABLE IF NOT EXISTS test_table (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入初始测试数据（与主库保持一致）
INSERT INTO test_table (name, description) VALUES 
('测试数据1', '这是主从复制测试数据1'),
('测试数据2', '这是主从复制测试数据2');

-- 确保更改已提交
COMMIT; 