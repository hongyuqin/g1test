package com.huf.g1test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;

    private String email;
}
/**
 *
 *
 * CREATE TABLE `blocks` (
 *   `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
 *   `block_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '箱子唯一标识 (系统生成、无业务含义)',
 *   `block_serial_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '箱子序号 (算法生成、格式示例：1)',
 *   `driver_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '接单司机ID',
 */