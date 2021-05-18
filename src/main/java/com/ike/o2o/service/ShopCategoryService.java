package com.ike.o2o.service;

import com.ike.o2o.dto.ImageHolder;
import com.ike.o2o.dto.ShopCategoryExecution;
import com.ike.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    ShopCategoryExecution getShopCategoryList(ShopCategory shopCategoryConditionParam);

    ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder shopCategoryImg);

    ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder shopCategoryImg);
}