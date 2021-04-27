package com.ike.o2o.config.redis;

import com.ike.o2o.cache.JedisPoolWriper;
import com.ike.o2o.cache.JedisUtil;
import com.ike.o2o.until.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis
 */
@Configuration
public class RedisConfiguration {

    @Value("${redis.pool.maxActive}")//最大连接数量
    private Integer maxTotal;

    @Value("${redis.pool.maxIdle}")//最大空闲
    private Integer maxIdle;

    @Value("${redis.pool.maxWait}")//最大等待时间
    private Long maxWaitMillis;

    @Value("${redis.pool.testOnBorrow}")//获取连接时检查有效性,默认是false
    private Boolean testOnBorrow;

    @Value("${redis.hostname}")
    private String redisHostName;//主机名称

    @Value("${redis.port}")
    private Integer redisPort;//主机端口

    @Value("${redis.pool.password}")
    private String redisPassword;

    @Value("${redis.pool.connTimeOut}")
    private int connTimeOut;

    @Autowired
    private JedisPoolConfig jedisPoolConfig; //redis配置文件对象

    @Autowired
    private JedisPoolWriper jedisWritePool;//redis线程池对象

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * Redis配置文件对象
     *
     * @return JedisPoolConfig
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);//最大连接数量
        jedisPoolConfig.setMaxIdle(maxIdle);//最大空闲
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);//最大等待时间
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);//获取连接时检查有效性,默认是false
        return jedisPoolConfig;
    }

    /**
     * Redis 线程池对象
     *
     * @return JedisPoolWriper
     */
    @Bean(name = "jedisWritePool")
    public JedisPoolWriper createJedisWritePool() {
        return new JedisPoolWriper(jedisPoolConfig, redisHostName, redisPort, connTimeOut, DESUtil.getDecryptString(redisPassword));
    }

    /**
     * Redis 工具类(与Redis服务器交互)
     *
     * @return JedisUtil
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisWritePool);
        return jedisUtil;
    }

    /**
     * 对key的操作
     *
     * @return Jedis$Keys
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        return jedisUtil.new Keys();
    }

    /**
     * 对数据结构String数据操作
     *
     * @return Jedis$Strings
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings creatJedisStrings() {
        return jedisUtil.new Strings();
    }
}
