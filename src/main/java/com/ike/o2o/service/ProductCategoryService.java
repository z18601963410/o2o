package com.ike.o2o.service;

import com.ike.o2o.dto.ProductCategoryExecution;
import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.entity.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(long currentShopId);

    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategories, long ShopId);

    ProductCategoryExecution removeProductCategory(ProductCategory productCategory);
}
