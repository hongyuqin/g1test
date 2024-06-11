package com.huf.g1test;

import com.alibaba.fastjson.JSON;
import com.huf.g1test.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
@Slf4j
class G1testApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void testCompare(){
		BigDecimal userAmount = new BigDecimal("0.0024");
		BigDecimal totalFee = new BigDecimal("1.000000000000000000");
		if (totalFee.compareTo(userAmount.setScale(5, RoundingMode.UP)) < 0) {
			System.out.println("haha");
		}
	}

	@Test
	public void testRedis(){
		User user = new User();
		user.setAge(12);
		user.setEmail("ssss@qq.com");
		redisTemplate.opsForValue().set("hong", JSON.toJSONString(user));
		String str = redisTemplate.opsForValue().get("hong");
		user = JSON.parseObject(str, User.class);
		log.info("user  : {}", user);
	}


	@Test
	public void testJwt(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", 32);
		System.out.println( "Bearer " + Jwts.builder()
				.setClaims(map)
				.setExpiration(new Date(Instant.now().toEpochMilli() + 100000000000L))//expire time is 7 days
				.signWith(SignatureAlgorithm.HS512, "snmhH32qahOkXHrTSBySlA==")
				.compact());
	}

	@Test
	public void testEncrypt() throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

		SecretKeySpec keySpec = new SecretKeySpec("2bfde271b18bf9da516386f000cf1fec".getBytes(StandardCharsets.UTF_8), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec("f6cf862bc0bdeeca".getBytes(StandardCharsets.UTF_8));

		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] encrypted = cipher.doFinal("hongyuqin".getBytes(StandardCharsets.UTF_8));

		log.info("word :{}", Hex.encodeHexString(encrypted));
	}

}
