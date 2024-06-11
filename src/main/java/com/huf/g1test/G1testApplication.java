package com.huf.g1test;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.huf.g1test.dao")
@Slf4j
public class G1testApplication {
	@Bean
	@SentinelRestTemplate
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



		public static void main(String[] args) {
		SpringApplication.run(G1testApplication.class, args);
		/*AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = ac.getBean(UserService.class);
		userService.printName();*/
	}

}
