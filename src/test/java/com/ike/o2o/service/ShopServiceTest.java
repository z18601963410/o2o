package com.ike.o2o.service;

import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testA_getShopList() {
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(3L);
        ShopCategory child = new ShopCategory();
        child.setParent(parent);

        Shop shopCondition=new Shop();
        shopCondition.setShopCategory(child);
        shopService.getShopList(shopCondition,0,99);
    }
}
