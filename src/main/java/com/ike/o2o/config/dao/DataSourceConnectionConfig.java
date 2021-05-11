package com.ike.o2o.config.dao;


import com.ike.o2o.until.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * 数据源帮助类和数据源配置类
 */
@Configuration
public class DataSourceConnectionConfig {
    //主库配置
    @Value("${jdbc.driver}")
    private String masterDriverClass;
    @Value("${jdbc.master.url}")
    private String masterUrl;
    @Value("${jdbc.username}")
    private String masterUsername;
    @Value("${jdbc.password}")
    private String masterPassword;

    //从库1
    @Value("${jdbc.driver}")
    private String slave1DriverClass;
    @Value("${jdbc.slave.url}")
    private String slave1Url;
    @Value("${jdbc.username}")
    private String slave1Username;
    @Value("${jdbc.password}")
    private String slave1Password;


    /**
     * 主库
     *
     * @return DataSource
     */
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        //配置数据源其他参数

        //返回数据源
        return DataSourceConnectionConfig.setDataSourceBaseConfig(
                comboPooledDataSource, masterDriverClass, masterUrl, masterUsername, masterPassword);
    }


    /**
     * 从库1
     *
     * @return 数据源对象
     * @throws PropertyVetoException 参数非法异常
     */
    @Bean("slave1")
    public DataSource salve1DataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        //配置数据源其他参数

        //返回数据源
        return DataSourceConnectionConfig.setDataSourceBaseConfig(comboPooledDataSource, slave1DriverClass, slave1Url, slave1Username, slave1Password);
    }

    /**
     * 从库2
     *
     * @return 数据源对象
     * @throws PropertyVetoException 参数非法异常
     */
    @Bean("slave2")
    public DataSource salve2DataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        //配置数据源其他参数

        //返回数据源
        return DataSourceConnectionConfig.setDataSourceBaseConfig(comboPooledDataSource, slave1DriverClass, slave1Url, slave1Username, slave1Password);
    }

    /**
     * 从库3
     *
     * @return 数据源对象
     * @throws PropertyVetoException 参数非法异常
     */
    @Bean("slave3")
    public DataSource salve3DataSource() throws PropertyVetoException {
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        //配置数据源其他参数

        //返回数据源
        return DataSourceConnectionConfig.setDataSourceBaseConfig(comboPooledDataSource, slave1DriverClass, slave1Url, slave1Username, slave1Password);
    }


    /**
     * 快速创建数据源对象
     * 数据源对象的基本配置
     *
     * @param dataSource   需要配置的数据源对象
     * @param jdbcDriver   驱动程序
     * @param jdbcUrl      数据库url
     * @param jdbcUsername 数据库用户名(加密)
     * @param jdbcPassword 数据库密码(加密)
     * @return 配置后的数据源对象
     * @throws PropertyVetoException 参数配置失败异常
     */
    private static DataSource setDataSourceBaseConfig(ComboPooledDataSource dataSource,
                                                      String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword) throws PropertyVetoException {
        // 驱动
        dataSource.setDriverClass(jdbcDriver);
        // 数据库连接URL
        dataSource.setJdbcUrl(jdbcUrl);
        // 设置用户名(解码)
        dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
        // 设置用户密码(解码)
        dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
        return dataSource;
    }

}
