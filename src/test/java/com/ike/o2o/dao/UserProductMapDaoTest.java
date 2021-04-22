package com.ike.o2o.dao;

import com.ike.o2o.BaseTest;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserProductMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMapDaoTest extends BaseTest {
    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void testA_insert() {
        PersonInfo userA = new PersonInfo();
        userA.setUserId(1L);
        PersonInfo userB = new PersonInfo();
        userB.setUserId(26L);

        Product productA = new Product();
        productA.setProductID(11L);
        Product productB = new Product();
        productB.setProductID(12L);
        Product productC = new Product();
        productC.setProductID(13L);

        Shop shopA = new Shop();
        shopA.setShopId(14L);
        Shop shopB = new Shop();
        shopB.setShopId(15L);

        PersonInfo operatorA = new PersonInfo();
        operatorA.setUserId(28L);
        PersonInfo operatorB = new PersonInfo();
        operatorB.setUserId(29L);

        UserProductMap userProductMapA = new UserProductMap();
        userProductMapA.setCreateTime(new Date());
        userProductMapA.setPoint(1000);

        userProductMapA.setUser(userA);
        userProductMapA.setProduct(productA);
        userProductMapA.setShop(shopA);
        userProductMapA.setOperator(operatorA);


        UserProductMap userProductMapB = new UserProductMap();
        userProductMapB.setCreateTime(new Date());
        userProductMapB.setPoint(999);

        userProductMapB.setUser(userA);
        userProductMapB.setProduct(productB);
        userProductMapB.setShop(shopA);
        userProductMapB.setOperator(operatorA);

        UserProductMap userProductMapC = new UserProductMap();
        userProductMapC.setCreateTime(new Date());
        userProductMapC.setPoint(66);

        userProductMapC.setUser(userA);
        userProductMapC.setProduct(productC);
        userProductMapC.setShop(shopB);
        userProductMapC.setOperator(operatorB);

        UserProductMap userProductMapD = new UserProductMap();
        userProductMapD.setCreateTime(new Date());
        userProductMapD.setPoint(888);

        userProductMapD.setUser(userB);
        userProductMapD.setProduct(productC);
        userProductMapD.setShop(shopA);
        userProductMapD.setOperator(operatorA);

        int affectA = userProductMapDao.insertUserProductMap(userProductMapA);
        int affectB = userProductMapDao.insertUserProductMap(userProductMapB);
        int affectC = userProductMapDao.insertUserProductMap(userProductMapC);
        int affectD = userProductMapDao.insertUserProductMap(userProductMapD);

        System.out.println(userProductMapA);

        assertEquals(affectA + affectB + affectC + affectD, 4);
    }

    @Test
    public void testB_queryAll() {
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo users = new PersonInfo();
        users.setUserId(1L);
        userProductMap.setUser(users);
        Shop shop = new Shop();
        shop.setShopId(14L);
        userProductMap.setShop(shop);
        PersonInfo operator = new PersonInfo();
        operator.setUserId(28L);
        userProductMap.setOperator(operator);
        List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMap(userProductMap, 0, 2);
        assertEquals(userProductMapList.size(),2);
    }

    @Test
    public void testB_queryCount() {
        UserProductMap userProductMap=new UserProductMap();
        Shop shop = new Shop();
        shop.setShopId(14L);
        userProductMap.setShop(shop);
        int count=userProductMapDao.queryUserProductMapCount(null);
        assertEquals(count,4);
    }

}
