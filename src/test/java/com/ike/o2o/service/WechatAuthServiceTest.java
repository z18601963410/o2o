package com.ike.o2o.service;

import com.ike.o2o.dto.WechatAuthExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
    @Autowired
    private WechatAuthService wechatAuthService;

    /**
     * WechatAuthExecution register(WechatAuth wechatAuth)
     * wechatAuth参数包含 personinfo对象,根据判断personinfo对象是否有userID来判断是否为首次访问,首次访问则注册一个personinfo
     * 赋值给wechatAuth,在注册微信用户
     * <p>
     * wechatAuth 必须必须包含PersonInfo对象
     * PersonInfo对象的name不能为空
     */
    @Test
    public void testARegister() {
        PersonInfo personInfo = new PersonInfo();
        //personInfo.setName("ike");
        personInfo.setUserId(26L);
        WechatAuth wechatAuth = new WechatAuth();
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId("test");
        wechatAuth.setCreateTime(new Date());

        WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);

        System.out.println(wechatAuthExecution.getWechatAuth());


    }

}
