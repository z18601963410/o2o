package com.ike.o2o.dao;

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
     * 新增店铺类别
     * @param shopCategoryList
     * @return
     */
    int insertShopCategoryList(List<ShopCategory> shopCategoryList);
}
