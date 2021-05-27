package com.ike.o2o.config.dao;

import com.ike.o2o.dao.split.DynamicDataSourceHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接池C3P0   @MapperScan 扫描dao包
 */
@Configuration
@MapperScan("com.ike.o2o.dao")
public class DataSourceConfiguration {

    //数据源Map
    private Map<Object, Object> DataSourceMap = new HashMap<>();

    /**
     * 构造函数注入数据库对象
     *
     * @param masterDataSource 主库  与beanName相同
     * @param slave1           从库1 与beanName相同
     */
    @Autowired
    public DataSourceConfiguration(DataSource masterDataSource, DataSource slave1) {
        //主库配置
        this.DataSourceMap.put(DynamicDataSourceHolder.DB_MASTER, masterDataSource);
        //从库:多数据源配置. key与value(数据源对象)关联>>动态数据源根据获得的KEY选择对应的数据源对象
        this.DataSourceMap.put(DynamicDataSourceHolder.DB_SLAVE, slave1);
    }


    /**
     * 数据源路由
     * <p>
     * 工作过程概述: 动态数据源对象的getDbType()方法从线程中获取key值,匹配对应的数据源对象(数据源对象包含在一个Map中)
     * 动态数据源对象配置一个Map,map的key用于数据源的匹配,value则是数据源对象
     * <p>
     * 需要动态数据源对象
     *
     * @return AbstractRoutingDataSource
     */
    @Bean(name="dynamicDataSource")
    public AbstractRoutingDataSource getDynamicDataSource() {
        //动态数据源对象
        AbstractRoutingDataSource dynamicDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                //动态数据源执行该方法获取数据源对应的key>>该key由mybatis拦截器设置在ThreadLocal<String> contextHolder中
                //拦截器对象会在SQL执行前对其拦截和解析,根据解析结果选择需要的数据源KEY配置到线程变量中
                return DynamicDataSourceHolder.getDbType();
            }
        };
        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(DataSourceMap.get(DynamicDataSourceHolder.DB_MASTER));
        //将数据源列表配置到动态数据源对象中
        dynamicDataSource.setTargetDataSources(DataSourceMap);
        //返回动态数据源对象
        return dynamicDataSource;
    }

    /**
     * 数据源(使用动态数据源对象)
     *
     * @return 延迟加载数据源
     */
    @Bean(name = "dataSource")
    public LazyConnectionDataSourceProxy getLazyConnectionDataSourceProxy(AbstractRoutingDataSource dynamicDataSource) {
        //数据源代理对象
        LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy = new LazyConnectionDataSourceProxy();
        //配置数据源
        lazyConnectionDataSourceProxy.setTargetDataSource(dynamicDataSource);
        //返回数据源
        return lazyConnectionDataSourceProxy;
    }

}
