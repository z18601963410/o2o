package com.ike.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 构建redis连接池对象
 */
public class JedisPoolWriper {
    //连接池对象
    private JedisPool jedisPool;

    /**
     * Spring创建该redis连接池对象,构造函数参数由spring-redis.xml赋值
     *
     * @param pooloolConfig 连接池配置参数
     * @param host          主机地址
     * @param port          端口号
     */
    public JedisPoolWriper(final JedisPoolConfig pooloolConfig, final String host, final int port,int timeout,String redisPassword) {
        try {
            //初始化连接池对象  public JedisPool(GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database)
            jedisPool = new JedisPool(pooloolConfig, host, port,timeout,redisPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
