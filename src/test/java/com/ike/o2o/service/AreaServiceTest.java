package com.ike.o2o.service;

import com.ike.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest  {

    @Autowired
    private AreaService areaService;
    //Could not autowire. No beans of 'AreaService' type found.

    @Test
    public void areaServiceTest() {
        List<Area> areaList = areaService.getAreaList();
        for (Area area : areaList) {
            System.out.println(area);
        }
    }

}
