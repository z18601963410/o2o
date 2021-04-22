package com.ike.o2o.dao;

import com.ike.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商品销量统计
 */
public interface ProductSellDailyDao {

    /**
     * 查询符合条件的商品销量统计数据
     *
     * @param productSellDaily 包含条件的实体
     * @param beginTime        开始时间
     * @param endTime          结束时间
     * @return 列表
     */
    List<ProductSellDaily> queryProductSellDaily(@Param("productSellDailyCondition") ProductSellDaily productSellDaily,
                                                 @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 统计平台所有商品的日销量
     *
     * @return 收到影响行数
     */
    int insertProductSellDaily();

    /**
     * 统计平台没有销量的商品
     *
     * @return 收到影响行数
     */
    int insertDefaultProductSellDaily();
}
