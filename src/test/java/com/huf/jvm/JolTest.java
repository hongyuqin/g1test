package com.huf.jvm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JolTest {
    @Test
    public void test(){
        double a = 218.841817493091894d;
        BigDecimal b = new BigDecimal("218.841817493091894");
        System.out.println(b);
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

    @Test
    public void testInt(){
        System.out.println(Integer.parseInt(null));
    }

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o){
                    System.out.println("test");
                    System.out.println(ClassLayout.parseInstance(o).toPrintable());
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("after one second");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
