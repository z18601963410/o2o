package com.ike.o2o.dao;

import com.ike.o2o.entity.Product;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.until.ImageUtil;
import com.ike.o2o.until.PathUtil;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest  {
    @Autowired
    private ProductDao productDao;

    @Test
    @Ignore
    public void testAInsertProduct() throws FileNotFoundException {
        Product product = new Product();

        Shop shop = new Shop();
        shop.setShopId(14L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(26L);

        File fileProductImg = new File("C:\\Users\\61447\\Desktop\\imgs\\二手商品交易\\1.jpg");

        product.setProductName("苹果奶昔");
        product.setProductDesc("苹果奶昔的描述");
        product.setImgAddr(null);
        product.setNormalPrice("500");
        product.setPromotionPrice("600");
        product.setPriority(300);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);
        product.setProductCategory(productCategory);
        product.setShop(shop);
        for (int i = 0; i < 5; i++) {
            int affect = productDao.insertProduct(product);
            assertTrue(affect > 0);
        }
    }

    @Test
    public void testBSelectProduct() {
        List<Product> productList = productDao.selectProductByShopId(2L);
        for (Product product : productList) {
            System.out.println("商品ID:" + product.getProductId() + ",商品包含的图片数量:" + product.getProductImgList().size());
        }
    }

    @Test
    @Ignore
    public void testCUpdateProductById() {
        Product product = new Product();
        product.setProductID(2L);
        Shop shop = new Shop();
        shop.setShopId(2L);

        product.setProductName("我修改了店铺名称007");


        //System.out.println(product);

        productDao.updateProduct(product, 2);

    }


    @Test
    @Ignore
    public void testDDeleteProduct() {
        long productId = 9L;
        long shopId = 5L;

        assertEquals(productDao.deleteProduct(productId, shopId), 1);
    }

    @Test
    @Ignore
    public void testUpdateProductCategoryIdToNull() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(15L);
        int affect = productDao.updateProductCategoryIdToNull(productCategory);

        System.out.println(affect);
    }

    @Test
    @Ignore
    public void testSelectProductList() {
        //shopId =18  productCategoryId=10  记录数:2
        Product product = new Product();
        Shop shop = new Shop();
        ProductCategory productCategory = new ProductCategory();

        shop.setShopId(18L);
        productCategory.setProductCategoryId(10L);
        product.setShop(shop);
        //product.setProductCategory(productCategory);
        //product.setProductName("优品");
        List<Product> productList = productDao.selectProductList(product, 0, 99);

        for (Product temp : productList) {
            System.out.println(temp);
        }
    }


}
