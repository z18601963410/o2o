package com.ike.o2o.dao;

import com.ike.o2o.BaseTest;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserShopMapDaoTest extends BaseTest {
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void testA_insert() {
        UserShopMap userShopMapA = new UserShopMap();
        UserShopMap userShopMapB = new UserShopMap();
        UserShopMap userShopMapC = new UserShopMap();
        PersonInfo userA = new PersonInfo();
        PersonInfo userB = new PersonInfo();
        PersonInfo userC = new PersonInfo();

        userA.setUserId(1L);
        userB.setUserId(26L);
        userC.setUserId(27L);

        Shop shopA = new Shop();
        Shop shopB = new Shop();

        shopA.setShopId(14L);
        shopB.setShopId(15L);

        userShopMapA.setCreateTime(new Date());
        userShopMapB.setCreateTime(new Date());
        userShopMapC.setCreateTime(new Date());

        userShopMapA.setPoint(1000);
        userShopMapB.setPoint(100);
        userShopMapC.setPoint(500);

        userShopMapA.setUser(userA);
        userShopMapB.setUser(userB);
        userShopMapC.setUser(userC);

        userShopMapA.setShop(shopA);
        userShopMapB.setShop(shopA);
        userShopMapC.setShop(shopB);

        int affectA = userShopMapDao.insertUserShopMap(userShopMapA);
        int affectB = userShopMapDao.insertUserShopMap(userShopMapB);
        int affectC = userShopMapDao.insertUserShopMap(userShopMapC);

        System.out.println(affectA);

        assertEquals(affectA + affectB + affectC, 3);
    }

    @Test
    public void testB_update() {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setUserShopId(3L);
        userShopMap.setPoint(99999);
        PersonInfo personInfo=new PersonInfo();
        personInfo.setUserId(27L);
        Shop shop=new Shop();
        shop.setShopId(15L);
        userShopMap.setUser(personInfo);
        userShopMap.setShop(shop);
        userShopMapDao.updateUserShopMap(userShopMap);
    }

    @Test
    public void testC_selectAll() {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setPoint(1000);
        List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopList(userShopMap, 0, 999);
        assertEquals(userShopMapList.size(), 2);
    }

    @Test
    public void testD_selectByUserIdAndShopId() {
        UserShopMap userShopMap = userShopMapDao.queryUserShopMapById(1L, 14L);
        assertEquals(userShopMap != null, true);
        System.out.println(userShopMap);

    }

    @Test
    public void testE_queryListCount() {
        int count = userShopMapDao.queryUserShopListCount(null);
        assertEquals(count, 3);
    }
}
