package com.ike.o2o.dao;

import com.ike.o2o.BaseTest;
import com.ike.o2o.entity.Award;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserAwardMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest extends BaseTest {
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Test
    public void testA_insert() {
        Shop shopA = new Shop();
        shopA.setShopId(14L);
        Shop shopB = new Shop();
        shopB.setShopId(15L);

        PersonInfo operatorA = new PersonInfo();
        operatorA.setUserId(28L);
        PersonInfo operatorB = new PersonInfo();
        operatorB.setUserId(29L);

        PersonInfo userA = new PersonInfo();
        userA.setUserId(1L);
        PersonInfo userB = new PersonInfo();
        userB.setUserId(26L);

        Award awardA = new Award();
        awardA.setAwardId(1L);
        Award awardB = new Award();
        awardB.setAwardId(2L);
        Award awardC = new Award();
        awardC.setAwardId(3L);

        UserAwardMap userAwardMapA = new UserAwardMap();
        UserAwardMap userAwardMapC = new UserAwardMap();
        UserAwardMap userAwardMapB = new UserAwardMap();

        userAwardMapA.setCreateTime(new Date());
        userAwardMapB.setCreateTime(new Date());
        userAwardMapC.setCreateTime(new Date());

        userAwardMapA.setAward(awardA);
        userAwardMapB.setAward(awardB);
        userAwardMapC.setAward(awardC);

        userAwardMapA.setOperator(operatorA);
        userAwardMapB.setOperator(operatorA);
        userAwardMapC.setOperator(operatorB);

        userAwardMapA.setPoint(100);
        userAwardMapB.setPoint(200);
        userAwardMapC.setPoint(50);

        userAwardMapA.setShop(shopA);
        userAwardMapB.setShop(shopA);
        userAwardMapC.setShop(shopB);

        userAwardMapA.setUsedStatus(1);
        userAwardMapB.setUsedStatus(1);
        userAwardMapC.setUsedStatus(1);

        userAwardMapA.setUser(userA);
        userAwardMapB.setUser(userB);
        userAwardMapC.setUser(userA);

        int affectA = userAwardMapDao.insertUserAwardMap(userAwardMapA);
        int affectB = userAwardMapDao.insertUserAwardMap(userAwardMapB);
        int affectC = userAwardMapDao.insertUserAwardMap(userAwardMapC);

        assertEquals(affectA + affectB + affectC, 3);

        System.out.println(userAwardMapA);
        System.out.println(userAwardMapB);
        System.out.println(userAwardMapC);
    }

    @Test
    public void testB_update() {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setPoint(10000);
        userAwardMap.setUsedStatus(0);
        userAwardMap.setUserAwardId(2L);

        int affect = userAwardMapDao.updateUserAwardMap(userAwardMap);
        assertEquals(affect, 1);
    }

    @Test
    public void testC_queryAll() {
        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(null, 0, 2);
        assertEquals(userAwardMapList.size(), 2);
    }

    @Test
    public void testD_queryReceived() {
        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryReceivedAwardMapList(null, 0, 999);
        assertEquals(userAwardMapList.size(), 2);
    }

    @Test
    public void testE_queryCount() {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setUsedStatus(1);
        int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        assertEquals(count, 2);
    }

    @Test
    public void testF_queryById() {
        UserAwardMap userAwardMap = userAwardMapDao.queryUserAwardMapById(1L);
        assertEquals(userAwardMap != null, true);
        System.out.println(userAwardMap);
    }
}
