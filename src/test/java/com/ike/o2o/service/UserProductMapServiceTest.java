package com.ike.o2o.service;

import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserProductMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

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
        Product product = new Product();
        product.setProductName("蛋糕");

        userProductMap.setProduct(product);

        UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductMap(userProductMap, 1, 999);

        for (UserProductMap temp : userProductMapExecution.getUserProductMapList()
        ) {
            System.out.println(temp);
        }
        System.out.println(userProductMapExecution.getCount());
    }

    @Test
    public void addUserProductMapTest() {
        UserProductMap userProductMap = new UserProductMap();
        //商品积分
        userProductMap.setPoint(1000);
        //顾客对象
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        userProductMap.setUser(user);
        //商品对象
        Product product = new Product();
        product.setProductID(52L);
        userProductMap.setProduct(product);
        //店铺对象
        Shop shop = new Shop();
        shop.setShopId(57L);
        userProductMap.setShop(shop);
        //操作员对象
        userProductMap.setOperator(user);
        userProductMap.setCreateTime(new Date());
        userProductMap.setLastEditTime(new Date());

        UserProductMapExecution userProductMapExecution= userProductMapService.addUserProductMap(userProductMap);

        System.out.println(userProductMapExecution.getState());
    }
}
