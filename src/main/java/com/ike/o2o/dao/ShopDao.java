package com.ike.o2o.dao;

import com.ike.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    /**
     * 新增店铺
     *
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     *
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     * 根据指定shopId查询店铺信息
     *
     * @param shopId
     * @return
     */
    Shop queryByShopId(Long shopId);

    /**
     * 获取shopList
     *
     * @param ShopCondition
     * @param rowIndex      页码值
     * @param pageSize      当前页对象数量
     * @return
     */
    List<Shop> queryShopList(@Param("ShopConditionParam") Shop ShopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 返回queryShopList总数
     *
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("ShopConditionParam") Shop shopCondition);

}
