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

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAQueryProductSellDaily() {
        Product product = new Product();
        Shop shop =new Shop();
        //shop.setShopId(14L);
        ProductSellDaily productSellDaily = new ProductSellDaily();
        productSellDaily.setProduct(product);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailyList=productSellDailyDao.queryProductSellDaily(productSellDaily, null,null);

        for(ProductSellDaily p:productSellDailyList){
            System.out.println(p.getShop().getShopId()+":"+p.getCreateTime());
        }
    }

    @Test
    public void testBInsertProductSellDaily() {
        productSellDailyDao.insertProductSellDaily();
    }

    @Test
    public void testCInsertDefaultProductSellDaily() {
        productSellDailyDao.insertDefaultProductSellDaily();
    }
}
