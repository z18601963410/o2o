package com.ike.o2o.dao;

import com.ike.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HeadLineDaoTest {
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void queryListCountTest() {
        int count = headLineDao.queryHeadLineListCount(new HeadLine());
        System.out.println(count);
    }

    @Test
    public void query() {
        HeadLine headLine = headLineDao.queryHeadLineByHeadLineId(1L);
        System.out.println(headLine);
    }

    @Test
    public void test() throws Exception {
        String name = "%E6%96%B0%E5%A2%9E%E4%BA%86%E4%B8%80%E4%B8%AA%E7%B1%BB";

        String newName =java.net.URLDecoder.decode(name,"UTF-8");

        System.out.println(newName);

    }
}
