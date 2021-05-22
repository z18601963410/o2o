package com.ike.o2o.dao;

import com.ike.o2o.entity.Area;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AreaDaoTest {

    //测试回环

    @Autowired
    private AreaDao areaDao;


    @Test
    public void testA_insert() {
        Area area = new Area();
        area.setAreaName("test");
        area.setPriority(100);
        area.setCreateTime(new Date());
        area.setLastEditTime(new Date());
        int affect = areaDao.insertArea(area);
        assertEquals(affect, 1);
    }

    @Test
    public void testB_update() {
        Area area = new Area();
        area.setAreaId(1);
        area.setPriority(200);
        area.setLastEditTime(new Date());
        int affect = areaDao.updateArea(area);
        assertEquals(affect, 1);
    }

    @Test
    public void testC_query() {
        List<Area> areaList = areaDao.queryArea();
        for (Area area : areaList
        ) {
            System.out.println(area);
        }
        assertTrue(areaList.size() > 0);
    }

    @Test
    public void testD_delete(){
        int affect = areaDao.deleteArea(9L);
        assertEquals(affect, 1);
    }

}
