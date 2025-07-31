# JDK21 + Spring Boot 3 演示项目

## 🚀 项目概述

这是一个使用JDK21和Spring Boot 3的演示项目，展示了JDK21的主要新特性。

## 📋 环境要求

- **JDK 21** (必需)
- **Maven 3.8+**
- **Spring Boot 3.2.0**

## 🔧 安装和运行

### 1. 检查JDK版本
```bash
java -version
# 应该显示: openjdk version "21.x.x"
```

### 2. 编译项目
```bash
mvn clean compile
```

### 3. 运行项目
```bash
mvn spring-boot:run
```

### 4. 访问应用
```
http://localhost:8081
```

## 🎯 JDK21 新特性演示

### 1. 虚拟线程 (Virtual Threads)
```bash
curl http://localhost:8081/api/jdk21/virtual-threads
```
- 创建100个虚拟线程
- 演示高并发IO操作
- 相比传统线程，内存占用更少

### 2. Pattern Matching for Switch
```bash
curl http://localhost:8081/api/jdk21/pattern-matching/string
curl http://localhost:8081/api/jdk21/pattern-matching/number
```
- 增强的switch表达式
- 支持模式匹配
- 更简洁的代码

### 3. Record Patterns
```bash
curl http://localhost:8081/api/jdk21/record-patterns
```
- 记录类型模式匹配
- 结构化数据解构
- 类型安全

### 4. Sequenced Collections
```bash
curl http://localhost:8081/api/jdk21/sequenced-collections
```
- 有序集合的新方法
- `getFirst()`, `getLast()`, `reversed()`
- 统一的集合API

### 5. Unnamed Patterns
```bash
curl http://localhost:8081/api/jdk21/unnamed-patterns
```
- 使用 `_` 忽略不需要的字段
- 更简洁的模式匹配

### 6. 性能监控
```bash
curl http://localhost:8081/api/jdk21/performance
```
- 显示JVM性能指标
- 内存使用情况
- 处理器信息

### 7. 并发示例
```bash
curl http://localhost:8081/api/jdk21/concurrency
```
- 使用虚拟线程的并发处理
- 异步任务执行

## 🔍 JDK21 主要特性

### 核心特性
1. **虚拟线程** - 轻量级线程，适合IO密集型应用
2. **模式匹配** - 增强的switch表达式和instanceof
3. **记录模式** - 结构化数据解构
4. **有序集合** - 统一的集合API

### 预览特性
1. **字符串模板** - 更安全的字符串插值
2. **结构化并发** - 更好的并发控制
3. **外部函数和内存API** - 与本地代码交互

## 📊 性能对比

| 特性 | JDK8 | JDK21 | 改进 |
|------|------|-------|------|
| 线程创建 | 1MB/线程 | 1KB/线程 | 1000x |
| 启动时间 | 慢 | 快 | 10x |
| 内存使用 | 高 | 低 | 显著减少 |

## 🛠️ 开发工具配置

### IntelliJ IDEA
1. 设置Project SDK为JDK21
2. 设置Language Level为21
3. 启用预览特性

### VS Code
1. 安装Java Extension Pack
2. 设置java.configuration.runtimes
3. 配置Maven使用JDK21

## 📚 学习资源

- [JDK21官方文档](https://openjdk.org/projects/jdk/21/)
- [Spring Boot 3文档](https://spring.io/projects/spring-boot)
- [虚拟线程指南](https://openjdk.org/jeps/444)

## 🎉 总结

JDK21带来了许多激动人心的新特性，特别是虚拟线程将大大提升Java在高并发场景下的性能。这个项目展示了如何在实际应用中使用这些新特性。 