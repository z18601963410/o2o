package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ProductSellDailyDao;
import com.ike.o2o.entity.ProductSellDaily;
import com.ike.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务: 每天统计一次商品销量
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        logger.info("quartz is running !");
        int affect = productSellDailyDao.insertProductSellDaily();
        productSellDailyDao.insertDefaultProductSellDaily();
        logger.info(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "的商品销售情况" + affect);
    }

    /**
     * 查询商品销量情况
     *
     * @param productSellDailyCondition 条件实体
     * @param beginTime                 开始时间
     * @param endTime                   结束时间
     * @return
     */
    @Override
    public List<ProductSellDaily> queryProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDaily(productSellDailyCondition, beginTime, endTime);
    }
}
