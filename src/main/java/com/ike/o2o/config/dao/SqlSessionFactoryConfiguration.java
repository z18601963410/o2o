package com.ike.o2o.config.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 数据库连接SqlSession
 */
@Configuration
public class SqlSessionFactoryConfiguration {
    //数据库源对象
    @Autowired
    private DataSource dataSource;

    //mybatis-config.xml配置文件路径
    private static String mybatisConfigFile;

    //mapper.xml文件路径
    private static String mapperPath;

    @Value("${mybatis_config_file}")
    public void setMybatisConfigFile(String mybatisConfigFile) {
        SqlSessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
    }
    @Value("${mapper_path}")
    public void setMapperPath(String mapperPath) {
        SqlSessionFactoryConfiguration.mapperPath = mapperPath;
    }

    //实体类所在的包
    @Value("${type_alias_package}")
    private  String typeAliasPackage;

    /**
     * 数据流连接工厂对象
     * @return 数据库连接工厂SqlSessionFactory
     */
    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
        //数据库连接工厂对象
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        //扫描mybatis-config.xml
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
        //扫描mapper.xml文件路径
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        //Dao文件名自动匹配Mapper文件
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        //设置datasource
        sqlSessionFactoryBean.setDataSource(dataSource);
        //设置typeAlias包扫描路径
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);

        //返回数据库连接工厂
        return sqlSessionFactoryBean;
    }
}
