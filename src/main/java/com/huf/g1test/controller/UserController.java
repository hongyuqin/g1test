package com.huf.g1test.controller;

import com.huf.g1test.entity.User;
import com.huf.g1test.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 业务接口：查询用户列表（Sharding-JDBC自动路由到主库）
     */
    @GetMapping("/business")
    public Map<String, Object> getUsersFromMaster() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getUsersFromMaster();
            result.put("success", true);
            result.put("data", users);
            result.put("message", "查询成功（Sharding-JDBC自动路由到主库）");
            result.put("source", "MASTER");
        } catch (Exception e) {
            log.error("查询失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 管理后台接口：查询用户列表（Sharding-JDBC自动路由到从库）
     */
    @GetMapping("/admin")
    public Map<String, Object> getUsersFromSlave() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getUsersFromSlave();
            result.put("success", true);
            result.put("data", users);
            result.put("message", "查询成功（Sharding-JDBC自动路由到从库）");
            result.put("source", "SLAVE");
        } catch (Exception e) {
            log.error("查询失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 添加用户（写操作，Sharding-JDBC自动路由到主库）
     */
    @PostMapping
    public Map<String, Object> addUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.addUser(user);
            result.put("success", success);
            result.put("message", success ? "添加用户成功（Sharding-JDBC自动路由到主库）" : "添加用户失败");
            result.put("source", "MASTER");
        } catch (Exception e) {
            log.error("添加用户失败", e);
            result.put("success", false);
            result.put("message", "添加失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 事务测试接口：事务中添加用户（Sharding-JDBC自动路由到主库）
     */
    @PostMapping("/transaction")
    public Map<String, Object> addUserWithTransaction(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.addUserWithTransaction(user);
            result.put("success", success);
            result.put("message", success ? "事务中添加用户成功（Sharding-JDBC自动路由到主库）" : "添加用户失败");
            result.put("source", "MASTER");
        } catch (Exception e) {
            log.error("事务中添加用户失败", e);
            result.put("success", false);
            result.put("message", "添加失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试接口：同时查询主库和从库数据
     */
    @GetMapping("/compare")
    public Map<String, Object> compareDataSources() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> masterUsers = userService.getUsersFromMaster();
            List<User> slaveUsers = userService.getUsersFromSlave();
            
            result.put("success", true);
            result.put("master", masterUsers);
            result.put("slave", slaveUsers);
            result.put("masterCount", masterUsers.size());
            result.put("slaveCount", slaveUsers.size());
            result.put("message", "数据对比完成（Sharding-JDBC自动路由）");
        } catch (Exception e) {
            log.error("数据对比失败", e);
            result.put("success", false);
            result.put("message", "对比失败: " + e.getMessage());
        }
        return result;
    }
} 