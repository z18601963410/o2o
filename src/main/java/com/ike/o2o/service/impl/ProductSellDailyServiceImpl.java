package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ProductSellDailyDao;
import com.ike.o2o.entity.ProductSellDaily;
import com.ike.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务: 每天统计一次商品销量
 */
@Service
//@EnableScheduling //开启定时任务支持,发现定时任务
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    private ProductSellDailyDao productSellDailyDao;

    @Autowired
    public ProductSellDailyServiceImpl(ProductSellDailyDao productSellDailyDao) {
        this.productSellDailyDao = productSellDailyDao;
    }

    //@Scheduled(cron = "0 * * * * ?")//配置为定时任务,spring会自动发现该任务>>每分钟执行一次 cron表达式 https://cron.qqe2.com/
    @Override
    public void dailyCalculate() {
        logger.info("quartz is running !");
        int insertProductSellDailyAffect = -1, insertDefaultProductSellDailyAffect = -1;
        try {
            insertProductSellDailyAffect = productSellDailyDao.insertProductSellDaily();
            insertDefaultProductSellDailyAffect = productSellDailyDao.insertDefaultProductSellDaily();
            logger.info(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "的商品销售情况" + insertProductSellDailyAffect);
            logger.info(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "的未出售商品数量" + insertDefaultProductSellDailyAffect);
        } catch (Exception e) {
            logger.info("dailyCalculate()方法未被执行insertProductSellDailyAffect={" + insertProductSellDailyAffect + "},insertDefaultProductSellDailyAffect={" + insertDefaultProductSellDailyAffect + "}");
            logger.error("dailyCalculate()方法执行出现异常!" + e.getMessage());
        }
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
