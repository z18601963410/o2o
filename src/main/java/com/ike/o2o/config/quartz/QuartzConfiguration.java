package com.ike.o2o.config.quartz;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务调度工厂对象
 *
 * 可以使用 @EnableScheduling @Scheduled(cron = "0 0/1 * * * ?")代替
 * 定时任务工具 : 统计商品销售情况
 * <p>
 * 分三部分: 定时任务对象, 触发器对象(绑定定时任务),调度任务工厂对象(绑定触发器)
 */
@Configuration
public class QuartzConfiguration {
    private Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    //触发器对象
    private CronTriggerFactoryBean productSellDailyTriggerFactory;

    @Autowired
    public QuartzConfiguration(CronTriggerFactoryBean productSellDailyTriggerFactory) {
        this.productSellDailyTriggerFactory = productSellDailyTriggerFactory;
    }

    /**
     * 创建调度工厂并返回
     *
     * @return SchedulerFactoryBean
     */
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        //配置触发器
        schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());
        logger.info("任务调度工厂schedulerFactory:" + schedulerFactory);
        return schedulerFactory;
    }
}
