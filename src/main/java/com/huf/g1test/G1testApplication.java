package com.huf.g1test;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.huf.g1test.mapper")
@Slf4j
public class G1testApplication {

	public static void main(String[] args) {
		SpringApplication.run(G1testApplication.class, args);
	}




}
