package com.ike.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement //开启事务支持 在service方法上添加Transactional即可
public class transactionManagerConfiguration implements TransactionManagementConfigurer {//声明式事务需要该接口
    /**
     * 事务管理器需要两个参数:  数据源 /  声明式事务注解
     */

    //需要数据源对象
    @Autowired
    private DataSource dataSource;

    /**
     * 创建事务管理器
     *
     * @return 返回PlatformTransactionManager的实现
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
