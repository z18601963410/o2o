package com.ike.o2o.dao;

import com.ike.o2o.entity.Award;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest {

    @Autowired
    private AwardDao awardDao;

    @Test
    public void testA_insertAward() {
        Award award = new Award();
        award.setShopId(14L);
        award.setAwardName("测试礼品");
        award.setAwardDesc("测试礼品描述");
        award.setAwardImg("测试礼品图片地址");
        award.setCreateTime(new Date());
        award.setLastEditTime(new Date());
        award.setEnableStatus(1);
        award.setPriority(100);
        award.setPoint(10);

        Award award1 = new Award();
        award1.setShopId(14L);
        award1.setAwardName("测试礼品1");
        award1.setAwardDesc("测试礼品描述");
        award1.setAwardImg("测试礼品图片地址");
        award1.setCreateTime(new Date());
        award1.setLastEditTime(new Date());
        award1.setEnableStatus(1);
        award1.setPriority(200);
        award1.setPoint(10);

        Award award2 = new Award();
        award2.setShopId(14L);
        award2.setAwardName("测试礼品2");
        award2.setAwardDesc("测试礼品描述");
        award2.setAwardImg("测试礼品图片地址");
        award2.setCreateTime(new Date());
        award2.setLastEditTime(new Date());
        award2.setEnableStatus(0);
        award2.setPriority(300);
        award2.setPoint(10);

        Award award3 = new Award();
        award3.setShopId(15L);
        award3.setAwardName("测试礼品3");
        award3.setAwardDesc("测试礼品描述");
        award3.setAwardImg("测试礼品图片地址");
        award3.setCreateTime(new Date());
        award3.setLastEditTime(new Date());
        award3.setEnableStatus(1);
        award3.setPriority(10);
        award3.setPoint(10);

        int affect1 = awardDao.insertAward(award);
        int affect2 = awardDao.insertAward(award1);
        int affect3 = awardDao.insertAward(award2);
        int affect4 = awardDao.insertAward(award3);

        assertEquals(affect1 + affect2 + affect3 + affect4, 4);
        System.out.println(award);
    }

    @Test
    public void testC_updateAward() {
        Award award = new Award();
        award.setAwardId(4L);
        award.setAwardDesc("我修改了商品描述");
        int affect = awardDao.updateAward(award, 15L);
        assertEquals(affect, 1);
    }

    @Test
    public void testD_queryAward() {
        Award award = new Award();
        List<Award> awardList = awardDao.queryAward(award,1,2);
        for (Award temp:awardList
             ) {
            System.out.println(temp);
        }
    }

    @Test
    public void testE_queryAwardById() {
        Award award = awardDao.queryAwardById(1L);
        assertTrue(award != null);
    }

    @Test
    public void testF_queryAwardCount() {
        Award award = new Award();
        award.setShopId(14L);
        //award.setAwardName("2");
        award.setEnableStatus(0);
        int count = awardDao.queryAwardCount(award);
        assertEquals(count, 1);
    }

    @Test
    public void testG_deleteAward() {
        int affect1=awardDao.deleteAward(1L,14L);
        int affect2=awardDao.deleteAward(2L,14L);
        int affect3=awardDao.deleteAward(3L,14L);
        int affect4=awardDao.deleteAward(4L,15L);

        assertEquals(affect1 + affect2 + affect3 + affect4, 4);
    }

}
