package com.ike.o2o.service;

import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserProductMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserProductMapServiceTest {
    @Autowired
    private UserProductMapService userProductMapService;

    @Test
    public void testA() {
        UserProductMap userProductMap = new UserProductMap();
        Shop shop = new Shop();
        shop.setShopId(57L);
        userProductMap.setShop(shop);
        Product product =new Product();
        product.setProductName("蛋糕");

        userProductMap.setProduct(product);

        UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductMap(userProductMap, 1, 999);

        for (UserProductMap temp:userProductMapExecution.getUserProductMapList()
        ) {
            System.out.println(temp);
        }
        System.out.println(userProductMapExecution.getCount());
    }
}
