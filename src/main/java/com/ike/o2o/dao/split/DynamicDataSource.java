package com.ike.o2o.dao.split;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        //返回数据源类型
        return DynamicDataSourceHolder.getDbType();
    }
}
