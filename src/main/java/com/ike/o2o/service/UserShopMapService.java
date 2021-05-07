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
     * @param userShopMapCondition 修改对象实体
     * @return 受影响行数
     */
    UserShopMapExecution updateUserShopMapPoint(UserShopMap userShopMapCondition) throws UserShopMapOperationException;

    /**
     * 查询userShopMap映射列表
     *
     * @param userShopMapCondition userShopMapCondition
     * @return 结果集
     */
    UserShopMapExecution queryUserShopMapListByShop(UserShopMap userShopMapCondition, int pageIndex, int pageSize);

    /**
     * 查询顾客在当前店铺的积分
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMapExecution queryUserShopMapByUserId(long userId, long shopId);

    /**
     * 查询顾客所有的积分
     *
     * @param userId 顾客ID
     * @return 顾客在所有店铺的积分总计
     */
    Integer queryUserPointById(long userId);
}
