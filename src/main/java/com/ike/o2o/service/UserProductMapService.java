package com.ike.o2o.service;

import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.UserProductMap;

public interface UserProductMapService {
    /**
     * 分页显示 顾客消费列表
     *
     * @param userProductMap 条件实体,一般是店铺
     * @param pageIndex      页码
     * @param pageSize       每页size
     * @return dto
     */
    UserProductMapExecution getUserProductMap(UserProductMap userProductMap, Integer pageIndex, Integer pageSize);
}
