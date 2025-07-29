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
     * 业务接口：从主库查询用户列表
     */
    @GetMapping("/business")
    public Map<String, Object> getUsersFromMaster() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getUsersFromMaster();
            result.put("success", true);
            result.put("data", users);
            result.put("message", "从主库查询成功");
            result.put("source", "MASTER");
        } catch (Exception e) {
            log.error("从主库查询失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 管理后台接口：从从库查询用户列表
     */
    @GetMapping("/admin")
    public Map<String, Object> getUsersFromSlave() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userService.getUsersFromSlave();
            result.put("success", true);
            result.put("data", users);
            result.put("message", "从从库查询成功");
            result.put("source", "SLAVE");
        } catch (Exception e) {
            log.error("从从库查询失败", e);
            result.put("success", false);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 添加用户（写操作，使用主库）
     */
    @PostMapping
    public Map<String, Object> addUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = userService.addUser(user);
            result.put("success", success);
            result.put("message", success ? "添加用户成功" : "添加用户失败");
            result.put("source", "MASTER");
        } catch (Exception e) {
            log.error("添加用户失败", e);
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
            result.put("message", "数据对比完成");
        } catch (Exception e) {
            log.error("数据对比失败", e);
            result.put("success", false);
            result.put("message", "对比失败: " + e.getMessage());
        }
        return result;
    }
} 