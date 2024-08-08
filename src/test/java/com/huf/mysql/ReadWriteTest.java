package com.huf.mysql;

import com.huf.g1test.G1testApplication;
import com.huf.g1test.entity.User;
import com.huf.g1test.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = G1testApplication.class)
public class ReadWriteTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert(){
        User user = new User();
        user.setAge(10);
        user.setName("hong");
        userMapper.insert(user);
    }

    @Test
    public void testSelect(){
        List<User> list = userMapper.selectList(null);
        System.out.println("user is : "+list);
    }

    @Test
    public void testInsertAndSelect(){
        testInsert();
        testSelect();
    }

}
