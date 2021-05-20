package com.ike.o2o.config.quartz;

import com.ike.o2o.service.ProductSellDailyService;
import com.ike.o2o.service.impl.ProductSellDailyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 * 配置定时任务工厂对象和触发器对象
 */
@Configuration
public class QuartzConfigAssist {
    private Logger logger = LoggerFactory.getLogger(QuartzConfigAssist.class);

    private ProductSellDailyService productSellDailyService;

    @Value("${quartz.cron}")
    private String cron;

    @Autowired
    public QuartzConfigAssist(ProductSellDailyService productSellDailyService) {
        this.productSellDailyService = productSellDailyService;
    }


    /**
     * 创建定时任务工厂对象
     *
     * @return MethodInvokingJobDetailFactoryBean
     */
    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {
        //定时任务工厂对象
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        // 设置jobDetail的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        // 设置jobDetail的组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        //多个定时任务是否并发执行
        jobDetailFactoryBean.setConcurrent(false);
        // 指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        // 指定运行任务的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
        logger.info("创建定时任务工厂对象jobDetailFactoryBean:" + jobDetailFactoryBean);
        return jobDetailFactoryBean;
    }

    /**
     * 创建cronTriggerFactory并返回,触发器
     *
     * @return CronTriggerFactoryBean
     */
    @Bean("productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTrigger() {
        // 创建triggerFactory实例，用来创建trigger
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        // 设置triggerFactory的名字
        triggerFactory.setName("product_sell_daily_trigger");
        // 设置triggerFactory的组名
        triggerFactory.setGroup("job_product_sell_daily_group");
        // 绑定jobDetail(定时任务对象)
        triggerFactory.setJobDetail(createJobDetail().getObject());
        // 设定cron表达式
        triggerFactory.setCronExpression(cron);
        logger.info("定时任务触发器productSellDailyTriggerFactory:" + triggerFactory);
        return triggerFactory;
    }
}
