package com.ike.o2o.dao;

import com.ike.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 查询商品分类列表
     *
     * @param ShopId 商铺ID
     * @return 商品分类列表
     */
    List<ProductCategory> queryProductCategoryList(@Param("ShopIdParam") Long ShopId);

    /**
     * 添加商品分类
     *
     * @param productCategories 商品分类列表
     * @return 受影响行数
     */
    int insertProductCategoryList(List<ProductCategory> productCategories);

    /**
     * 删除商铺商品分类
     *
     * @param productCategoryCondition 商品分类对象
     * @return 受影响行数
     */
    int deleteProductCategory(@Param("productCategoryParam") ProductCategory productCategoryCondition);
}
