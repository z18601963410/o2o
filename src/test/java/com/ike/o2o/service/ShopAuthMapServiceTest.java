package com.ike.o2o.service;

import com.ike.o2o.dto.ShopAuthMapExecution;
import com.ike.o2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShopAuthMapServiceTest {
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Test
    public void testAShopAuthMapServiceGet() {
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.queryShopAuthMapList(57L, 0, 99);

        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            System.out.println(shopAuthMap);
        }
    }
}
