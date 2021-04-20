package com.ike.o2o.dao.split;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

/**
 * 返回需要采用的数据源信息
 */
public class DynamicDataSourceHolder {
    //记录日志
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);

    //记录请求类型信息  线程变量
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    //主从库 数据源
    static final String DB_MASTER = "master";
    static final String DB_SLAVE = "slave";


    static Object getDbType() {
        //获取数据库数据源
        String db = contextHolder.get();
        if (db == null) {
            db = DB_MASTER;
        }
        return db;
    }

    /**
     * 设置线程的DBType
     *
     * @param str
     */
    static void setDbType(String str) {
        logger.debug("所使用数据源:" + str);
        contextHolder.set(str);
    }

    /**
     * 清理数据源
     */
    public static void clearDBType() {
        contextHolder.remove();
    }
}
