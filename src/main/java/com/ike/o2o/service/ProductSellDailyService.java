package com.ike.o2o.service;

import com.ike.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {
    /**
     * 统计商品销量
     */
    void dailyCalculate();

    /**
     * 统计平台商品销售情况
     *
     * @param productSellDailyCondition 条件实体
     * @param beginTime                 开始时间
     * @param endTime                   结束时间
     * @return 结果集
     */
    List<ProductSellDaily> queryProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);

}
