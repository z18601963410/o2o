package com.ike.o2o.service;

import com.ike.o2o.dto.AwardExecution;
import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.entity.Award;
import com.ike.o2o.enums.AwardStateEnum;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class AwardServiceTest {
    @Autowired
    private AwardService awardService;

    @Test
    public void testA_insertAward() throws Exception {
        File imageFile = new File("C:\\Users\\61447\\Desktop\\imgs\\水果\\1.jpeg");
        ImageHolder image = new ImageHolder("1.jpeg", new FileInputStream(imageFile));

        Award award = new Award();
        award.setAwardName("新增奖品001");
        award.setShopId(57L);
        award.setAwardDesc("描述");
        award.setPoint(999);
        award.setPriority(999);

        AwardExecution awardExecution = awardService.addAward(award, image);

        assertEquals(awardExecution.getState(), AwardStateEnum.SUCCESS.getState());

        System.out.println(awardExecution.getAward());
    }
    @Test
    public void testB_edit() throws Exception {
        File imageFile = new File("C:\\Users\\61447\\Desktop\\imgs\\水果\\5af4b8bc971686e9b5873297e86d22bb.jpeg");
        ImageHolder image = new ImageHolder("1.jpeg", new FileInputStream(imageFile));
        Award award = new Award();
        award.setAwardId(12L);
        award.setEnableStatus(1);
        award.setShopId(57L);
        AwardExecution awardExecution = awardService.editAward(award, image);
        System.out.println(awardExecution.getState());
    }

    @Test
    public void testC_queryByShopId() {
        Award awardCondition = new Award();
        awardCondition.setShopId(57L);
        AwardExecution awardExecution = awardService.queryAwardListByShop(awardCondition, 0, 99);

        System.out.println(awardExecution.getState());

        for (Award temp : awardExecution.getAwardList()
        ) {
            if (temp.getEnableStatus() == 1) {
                System.out.println("奖品id:" + temp.getAwardId() + "----奖品名称:" + temp.getAwardName());
            }
        }

    }

    @Test
    public void testD_queryByAwardId() {
        Award award = awardService.queryAwardByAwardId(1L);
        System.out.println(award);
    }

    @Test
    public void testE_deleteById() {
        AwardExecution awardExecution = awardService.removeAward(57L, 2L);
        System.out.println(AwardStateEnum.SUCCESS.getState() == awardExecution.getState());
    }
}
