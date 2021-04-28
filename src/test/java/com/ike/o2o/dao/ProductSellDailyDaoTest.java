package com.ike.o2o.dao;

import com.ike.o2o.BaseTest;
import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductSellDaily;
import com.ike.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAQueryProductSellDaily() {

        List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDaily(null, null, null);
        HashSet<Date> nameSet = new LinkedHashSet<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (ProductSellDaily p : productSellDailyList) {
            nameSet.add(p.getCreateTime());
            //System.out.println("商品ID:"+p.getProduct().getProductId()+"商品售出时间:"+simpleDateFormat.format(p.getCreateTime()));
        }
        for (Date date :
                nameSet) {
            System.out.println(simpleDateFormat.format(date));
        }
    }

    @Test
    public void testBInsertProductSellDaily() {
        int affect = productSellDailyDao.insertProductSellDaily();
        System.out.println(affect);
    }

    @Test
    public void testCInsertDefaultProductSellDaily() {
        int affect = productSellDailyDao.insertDefaultProductSellDaily();
        System.out.println(affect);
    }
}
