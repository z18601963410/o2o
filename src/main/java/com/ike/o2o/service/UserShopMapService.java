package com.ike.o2o.service;

import com.ike.o2o.dto.UserShopMapExecution;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.exception.UserShopMapOperationException;

/**
 * 积分查询
 */
public interface UserShopMapService {
    /**
     * 新增顾客店铺映射对象
     *
     * @param userShopMap 映射对象实体
     * @return 受影响行数
     */
    UserShopMapExecution insertUserShopMap(UserShopMap userShopMap) throws UserShopMapOperationException;

    /**
     * update顾客积分: 消费商品时增加积分,消费礼品时消耗积分
     *
     * @param pointChangeValue 需要操作的积分值
     * @return 受影响行数
     */
    UserShopMapExecution updateUserShopMapPoint(int pointChangeValue) throws UserShopMapOperationException;

    /**
     * 查询userShopMap映射列表
     *
     * @param userShopMapCondition 条件实体
     * @return 结果集
     */
    UserShopMapExecution queryUserShopMapListByShop(UserShopMap userShopMapCondition, int pageIndex, int pageSize);
}
