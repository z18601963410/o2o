package com.ike.o2o.service;

import com.ike.o2o.dto.UserAwardMapExecution;
import com.ike.o2o.entity.UserAwardMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserAwardMapServiceTest {
    @Autowired
    private UserAwardMapService userAwardMapService;

    @Test
    public void testA_update() {
        //变更状态信息
        UserAwardMap userAwardMap = new UserAwardMap();
        //修改后状态值
        userAwardMap.setUsedStatus(1);
        //从二维码对象中获取UserAwardId
        userAwardMap.setUserAwardId(21L);
        UserAwardMapExecution userAwardMapExecution = userAwardMapService.updateUserAwardMap(userAwardMap);
        System.out.println(userAwardMapExecution.getState());
    }
}
