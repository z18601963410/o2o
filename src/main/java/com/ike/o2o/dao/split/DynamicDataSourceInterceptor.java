package com.ike.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import javax.xml.transform.Result;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * 请求拦截器,对请求的类型进行判断是 select(query)?  update?(增删改底层通过update实现)
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {
    //记录日志
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    //正则表达式
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取参数列表
        Object[] objects = invocation.getArgs();
        //获取第一个参数
        MappedStatement mappedStatement = (MappedStatement) objects[0];
        //默认属于主库
        String lookUpKey = DynamicDataSourceHolder.DB_MASTER;

        //判断请求是否属于事务的
        boolean synchronizationAction = TransactionSynchronizationManager.isActualTransactionActive();
        if (!synchronizationAction) {
            //判断请求类型 读方法
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //selectKey 为自增id查询主键(SELECT LAST_INSERT_ID())方法,使用主库; 执行增删改操作返回id值
                if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    //使用主库
                    lookUpKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
                    //格式化sql语句,将换行等替换为空格
                    String sql = boundSql.getSql().toLowerCase(Locale.CANADA).replaceAll("[\\t\\n\\r]", " ");
                    //匹配正则表达式
                    if (sql.matches(REGEX)) {
                        //数据源设置为主库
                        lookUpKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookUpKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        } else {
            //非事务管理的,使用主库
            lookUpKey = DynamicDataSourceHolder.DB_MASTER;
        }
        //记录日志
        logger.debug("设置方法[{}]use [{}] strategy,SqlCommonType[{}]..",
                mappedStatement.getId(), lookUpKey, mappedStatement.getSqlCommandType());
        DynamicDataSourceHolder.setDbType(lookUpKey);
        return invocation.proceed();
    }

    /**
     * 返回本体或者代理对象
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {//对拦截的对象的类型进行判断,是则将intercept织入到拦截的对象当中去
            return Plugin.wrap(target, this);
        }
        return target;//否,则返回对象本体
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
