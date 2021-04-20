package com.ike.o2o.dao;

import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthMapDaoTest {
    @Autowired
    private ShopAuthMap shopAuthMap;

    @Test
    public void testA_insertShopAuth(){
        ShopAuthMap shopAuthMap=new ShopAuthMap();
        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());

        shopAuthMap.setTitle("老板娘");
        shopAuthMap.setTitleFlag(1001);
        shopAuthMap.setEnableStatus(1);

        PersonInfo personInfo=new PersonInfo();
        personInfo.setUserId(1L);
        personInfo.setName("username");
        Shop shop=new Shop();
        shop.setShopId(14L);
        shop.setShopName("张三");

        shopAuthMap.setShop(shop);
        shopAuthMap.setEmployee(personInfo);




    }
    @Test
    public void testB_updateShopAuth(){

    }
    @Test
    public void testC_queryShopAuthById(){

    }
    @Test
    public void testD_queryShopAuthCountByShopId(){

    }
    @Test
    public void testE_queryShopAuthMapListByShopId(){

    }
    @Test
    public void testF_deleteShopAuthById(){

    }
}
