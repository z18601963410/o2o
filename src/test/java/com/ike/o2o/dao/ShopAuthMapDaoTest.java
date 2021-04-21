package com.ike.o2o.dao;

import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopAuthMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopAuthMapDaoTest {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;


    @Test
    public void testA_insertShopAuth() {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());

        shopAuthMap.setTitle("老板娘");
        shopAuthMap.setTitleFlag(1001);
        shopAuthMap.setEnableStatus(1);

        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("username");
        Shop shop = new Shop();
        shop.setShopId(14L);
        shop.setShopName("张三");

        shopAuthMap.setShop(shop);
        shopAuthMap.setEmployee(user);

        for (int i = 0; i < 4; i++) {
            assertEquals(shopAuthMapDao.insertShopAuth(shopAuthMap), 1);
        }

    }

    @Test
    public void testB_updateShopAuth() {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setShopAuthId(1L);
        shopAuthMap.setTitle("BOSS");
        assertEquals(shopAuthMapDao.updateShopAuth(shopAuthMap), 1);
    }

    @Test
    public void testC_queryShopAuthById() {
        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthById(1L);
        assertEquals(shopAuthMap.getShopAuthId().equals(1L), true);
    }

    @Test
    public void testD_queryShopAuthCountByShopId() {
        int count = shopAuthMapDao.queryShopAuthCountByShopId(14L);
        assertEquals(count, 4);
    }

    @Test
    public void testE_queryShopAuthMapListByShopId() {
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(14L, 2, 3);
        assertEquals(shopAuthMapList.size(), 2);
    }

    @Test
    public void testF_queryShopAuthByCondition() {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setEnableStatus(1);
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthByCondition(shopAuthMap, 0, 99);
        assertEquals(shopAuthMapList.size(), 4);

    }

    @Test
    public void testG_deleteShopAuthById() {
        int affect1= shopAuthMapDao.deleteShopAuthById(1L);
        int affect2= shopAuthMapDao.deleteShopAuthById(2L);
        int affect3= shopAuthMapDao.deleteShopAuthById(3L);
        int affect4= shopAuthMapDao.deleteShopAuthById(4L);
        assertEquals(affect1=affect2=affect3=affect4, 1);
    }
}
