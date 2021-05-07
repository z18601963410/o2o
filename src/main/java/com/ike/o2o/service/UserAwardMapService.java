package com.ike.o2o.service;

import com.ike.o2o.dto.UserAwardMapExecution;
import com.ike.o2o.entity.UserAwardMap;
import com.ike.o2o.exception.UserAwardMapOperationException;

/**
 * 顾客奖品查询,兑换记录查询
 */
public interface UserAwardMapService {
    /**
     * 查询当前店铺下所有积分兑换记录
     *
     * @param userAwardMapCondition 条件实体
     * @param pageIndex             分页页码
     * @param pageSize              分页size
     * @return 查询结果
     */
    UserAwardMapExecution queryUserAwardMapByShop(UserAwardMap userAwardMapCondition, int pageIndex, int pageSize);

    /**
     * 新增积分顾客积分记录
     *
     * @param userAwardMap 条件实体
     * @return DTO
     * @throws UserAwardMapOperationException 运行异常,用于数据回滚
     */
    UserAwardMapExecution insertUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

    /**
     * 查询指定顾客礼品映射对象
     *
     * @param userAwardId userAwardId
     * @return UserAwardMap
     */
    UserAwardMapExecution queryUserAwardMapById(Long userAwardId);

    UserAwardMapExecution updateUserAwardMap(UserAwardMap userAwardMapCondition);
}
