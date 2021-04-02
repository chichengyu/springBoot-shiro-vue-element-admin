package com.shiro.dao;


import com.shiro.pojo.SysUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SysUserDaoTest {

    @Autowired
    private SysUserDao userDao;

    @Test
    public void findByUsername(){
//        String username = "admin";
//        SysUser user = userDao.findByUsername(username);
//        Assert.assertNotNull(username);
    }
}
