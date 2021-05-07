package com.ike.o2o.dao;

import com.ike.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAwardMapDao {
    /**
     * 查询符合条件的用户奖品列表
     *
     * @param userAward 条件实体
     * @param rowIndex  行号
     * @param pageSize  每页数量
     * @return 用户奖品list
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAward, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    /**
     * 查询符合条件的已领取奖品列表信息
     *
     * @param userAward 条件实体
     * @param rowIndex  行号
     * @param pageSize  每页数量
     * @return 用户奖品已兑换list
     */
    List<UserAwardMap> queryReceivedAwardMapList(@Param("userAwardCondition") UserAwardMap userAward, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的用户礼品映射列表长度
     *
     * @param userAwardMap 条件实体
     * @return 长度
     */
    int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardMap);

    /**
     * 根据id查询userAward
     *
     * @param userAwardId id
     * @return 用户奖品实体
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * 新增用户奖品映射对象
     *
     * @param userAwardMap 实体
     * @return 受影响行数
     */
    int insertUserAwardMap(@Param("userAwardCondition") UserAwardMap userAwardMap);


    /**
     * 修改用户奖品映射对象
     *
     * @param userAwardMap 实体
     * @return 受影响行数
     */
    int updateUserAwardMap(@Param("userAwardMapCondition") UserAwardMap userAwardMap);
}
