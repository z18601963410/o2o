package com.ike.o2o.dao;

import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserShopMapDao {
    /**
     * 条件查询符合要求的分页数据
     *
     * @param userShopMap 条件实体
     * @param rowIndex    页码
     * @param pageSize    每页数量
     * @return list
     */
    List<UserShopMap> queryUserShopList(@Param("userShopMapCondition") UserShopMap userShopMap, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件数据的长度
     *
     * @param userShopMap 条件实体
     * @return ListSize
     */
    int queryUserShopListCount(@Param("userShopMapCondition") UserShopMap userShopMap);

    /**
     * 根据userID和shopID查询符合条件的映射对象
     *
     * @param userId userId
     * @param shopId shopId
     * @return 符合条件的映射对象
     */
    UserShopMap queryUserShopMapById(@Param("userId") long userId, @Param("shopId") long shopId);

    /**
     * 插入userShopMap
     *
     * @param userShopMap 插入对象实体
     * @return 受影响行数
     */
    int insertUserShopMap(UserShopMap userShopMap);

    /**
     * 更新userShopMap
     *
     * @param userShopMap update对象实体
     * @return 受影响行数
     */
    int updateUserShopMap(UserShopMap userShopMap);

}
