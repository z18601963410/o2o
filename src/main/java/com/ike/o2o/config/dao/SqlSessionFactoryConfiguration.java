package com.ike.o2o.config.dao;


import com.ike.o2o.dao.split.DynamicDataSourceInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 数据库连接工厂对象配置
 */
@Configuration
public class SqlSessionFactoryConfiguration {

    //配置文件路径(未使用)
    @Value("${mybatis_config_file}")
    private String mybatisConfigFile;

    //映射文件路径
    private static String mapperPath;
    //实体包路径
    @Value("${type_alias_package}")
    private String typeAliasPackage;

    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SqlSessionFactoryConfiguration.mapperPath = mapperPath;
    }

    //数据源对象
    private DataSource dataSource;

    /**
     * 构造函数注入数据源
     *
     * @param dataSource 数据源对象
     */
    @Autowired
    public SqlSessionFactoryConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * SqlSessionFactory配置
     *
     * @param configuration 配置对象
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(org.apache.ibatis.session.Configuration configuration) throws Exception {
        //数据库连接工厂对象
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //设置配置对象(未使用) 拦截器配置 方式三
        //sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));

        //设置配置对象
        sqlSessionFactoryBean.setConfiguration(configuration);

        //设置映射文件路径
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        //Dao文件名自动匹配Mapper文件
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));

        //实体包路径
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);

        //设置拦截器插件(通过xml配置) 拦截器配置 方式一
        //sqlSessionFactoryBean.setPlugins(new DynamicDataSourceInterceptor());

        //数据源
        sqlSessionFactoryBean.setDataSource(dataSource);

        //返回SqlSessionFactory
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置Bean 读取application.yaml参数
     *
     * @return Configuration
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        //配置对象
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //配置拦截器 方式二
        configuration.addInterceptor(new DynamicDataSourceInterceptor());
        //返回配置文件对象
        return configuration;
    }
}
