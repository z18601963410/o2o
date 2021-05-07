package com.ike.o2o.service;

import com.ike.o2o.dto.UserShopMapExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.enums.UserShopMapStateEnum;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class UserShopMapServiceTest {

    @Autowired
    private UserShopMapService userShopMapService;

    @Test
    public void testA_selectAll() {
        UserShopMap userShopMap=new UserShopMap();
        Shop shop=new Shop();
        shop.setShopId(56L);
        PersonInfo user=new PersonInfo();
        user.setName("root");

        //userShopMap.setUser(user);
        userShopMap.setShop(shop);

        UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapListByShop(userShopMap, 0, 99);
        for (UserShopMap temp : userShopMapExecution.getUserShopMapList()
        ) {
            System.out.println(temp.getUser().getName());
        }
    }

    @Test
    public void testB_insert() {
        UserShopMap userShopMap = new UserShopMap();
        Shop shop = new Shop();
        shop.setShopId(57L);
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);

        userShopMap.setShop(shop);
        userShopMap.setUser(user);
        userShopMap.setPoint(100);

        UserShopMapExecution userShopMapExecution = userShopMapService.insertUserShopMap(userShopMap);

        System.out.println(userShopMapExecution.getUserShopMap());

    }

    @Test
    public void testC_update() {
        Shop shop = new Shop();
        shop.setShopId(57L);

        PersonInfo user = new PersonInfo();
        user.setUserId(28L);

        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setPoint(-500);
        userShopMap.setUser(user);
        userShopMap.setShop(shop);

        UserShopMapExecution userShopMapExecution = userShopMapService.updateUserShopMapPoint(userShopMap);

        System.out.println(userShopMapExecution.getState());
    }
}
