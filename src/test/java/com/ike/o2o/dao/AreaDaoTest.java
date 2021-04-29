package com.ike.o2o.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ike.o2o.dto.WechatInfo;
import com.ike.o2o.entity.Area;
import com.ike.o2o.until.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea() {
        List<Area> areaList = areaDao.queryArea();
    }

    @Test
    public void test() throws Exception {  //customerId  productId userAwardId  createTime  shopId

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date endTime = calendar.getTime();
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        Date startTime = calendar.getTime();
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("startTime :"+sim.format(startTime));
        System.out.println("startTime :"+sim.format(endTime));
    }

}
