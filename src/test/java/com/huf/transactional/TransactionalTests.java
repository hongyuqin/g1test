package com.huf.transactional;

import com.huf.g1test.G1testApplication;
import com.huf.g1test.dao.UserDao;
import com.huf.g1test.pojo.User;
import com.huf.g1test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = G1testApplication.class)
public class TransactionalTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;



    @Test
    public void test1(){
        List<User> list = userDao.selectList(null);
        list.forEach(System.out::println);
    }

    //测试事务回滚 : 抛出异常然后回滚了
    @Test
    public void testRollback(){
        try {
            userService.addUser1();
        } catch (RuntimeException e) {
            System.out.println("error is : "+e.getMessage());
        }
    }

    @Test
    public void test2(){
        userService.multiAddUser();
    }
}
