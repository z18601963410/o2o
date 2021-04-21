package com.ike.o2o.service;

import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class productServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testInsertProduct() {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(14L);
        product.setProductName("CESHI001");
        product.setShop(shop);
        product.setPoint(99);
        productService.addProduct(product, null, null);
    }
}
