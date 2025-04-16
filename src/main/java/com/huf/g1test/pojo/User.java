package com.huf.g1test.pojo;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private Integer age;

    private String email;
}
/**
create table user(
 `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
 `name` varchar(64)   NOT NULL,
 `age` int NOT NULL DEFAULT '25' ,
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=594 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci



 */