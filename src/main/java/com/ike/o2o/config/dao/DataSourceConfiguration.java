package com.ike.o2o.config.dao;

import com.ike.o2o.until.DESUtil;
import com.ike.o2o.until.MD5;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 * 数据库连接池
 */
@Configuration //Bean的来源,避免多例
@MapperScan("com.ike.o2o.dao")//描定义包下的所有的接口,并生成对应的bean对象
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")//数据库驱动
    private String jdbcDriver;

    @Value("${jdbc.master.url}")//主数据库url
    private String jdbcMasterUrl;

    @Value("${jdbc.slave.url}")//从数据库url
    private String jdbcSlaveUrl;

    @Value("${jdbc.username}")//数据库用户名
    private String jdbcUsername;

    @Value("${jdbc.password}")//数据库密码
    private String jdbcPassword;

    /**
     * 生成一个dataSource(数据库连接池)交由Spring管理
     *
     * @return 数据库连接池对象
     */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        //数据库连接池对象c3p0
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        //数据库驱动程序
        comboPooledDataSource.setDriverClass(jdbcDriver);
        //数据库URL
        comboPooledDataSource.setJdbcUrl(jdbcMasterUrl);
        //数据库username
        comboPooledDataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
        //数据库密码
        comboPooledDataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));

        //配置c3p0连接池私有属性
        //最大线程数
        comboPooledDataSource.setMaxPoolSize(30);
        //最小线程数
        comboPooledDataSource.setMinPoolSize(10);

        comboPooledDataSource.setInitialPoolSize(10);
        //关闭连接后不自动commit
        comboPooledDataSource.setAutoCommitOnClose(false);
        //连接超时时间
        comboPooledDataSource.setCheckoutTimeout(10000);
        //连接失败重试次数
        comboPooledDataSource.setAcquireRetryAttempts(2);

        //返回数据库连接池
        return comboPooledDataSource;
    }
}
