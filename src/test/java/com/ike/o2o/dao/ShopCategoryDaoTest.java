package com.ike.o2o.dao;


import com.ike.o2o.entity.ShopCategory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopCategoryDaoTest {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testA_query() {
        ShopCategory shopCategory = shopCategoryDao.queryShopCategoryById(9L);
        System.out.println(shopCategory);
    }

    @Test
    public void testB_edit() {
        ShopCategory shopCategory = shopCategoryDao.queryShopCategoryById(9L);
        shopCategory.setShopCategoryName("二手商品买卖new");
        shopCategory.setShopCategoryDesc("二级分类商品描述1new");
        shopCategory.setShopCategoryImg("/upload/images/item/shopCategory/二手市场.png");
        shopCategory.setPriority(1000);
        shopCategory.setLastEditTime(new Date());
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(4L);
        shopCategory.setParent(parent);

        int affect = shopCategoryDao.updateShopCategory(shopCategory);
        assertEquals(affect, 1);
    }

    @Test
    public void testC_insert() {
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryName("newinsert");
        shopCategory.setShopCategoryDesc("newinsert");
        shopCategory.setShopCategoryImg("newinsert.png");
        shopCategory.setPriority(99);
        shopCategory.setLastEditTime(new Date());
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(3L);
        shopCategory.setParent(parent);

        int affect = shopCategoryDao.insertShopCategory(shopCategory);
        assertEquals(affect, 1);

    }
}
