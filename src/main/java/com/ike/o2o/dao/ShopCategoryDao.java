package com.ike.o2o.dao;

import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {
    /**
     * 获取店铺分类信息
     *
     * @param
     * @return
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryConditionParam") ShopCategory shopCategoryCondition);

    /**
     * 根据ID查询商铺分类对象
     *
     * @param shopCategoryId shopCategoryId
     * @return  ShopCategory
     */
    ShopCategory queryShopCategoryById(long shopCategoryId);

    /**
     * 新增店铺类别列表
     *
     * @param shopCategoryList
     * @return
     */
    int insertShopCategoryList(List<ShopCategory> shopCategoryList);

    /**
     * 更新商铺分类信息
     *
     * @param shopCategory 分类对象
     * @return 受影响行数
     */
    int updateShopCategory(@Param("shopCategory") ShopCategory shopCategory);

    /**
     * 新增商铺分类对象
     *
     * @param shopCategory 分类对象
     * @return 受影响行数
     */
    int insertShopCategory(@Param("shopCategory") ShopCategory shopCategory);
}
