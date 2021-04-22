package com.ike.o2o.dao;

import com.ike.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProductMapDao {

    /**
     * 查询符合条件的 顾客商品信息(添加到购物车)
     *
     * @param userProductMap 顾客商品条件实体
     * @return 顾客商品列表
     */
    List<UserProductMap> queryUserProductMap(@Param("userProductMapCondition") UserProductMap userProductMap,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的 顾客商品信息长度
     *
     * @param userProductMap 查询条件实体
     * @return 长度
     */
    int queryUserProductMapCount(@Param("userProductMapCondition") UserProductMap userProductMap);

    /**
     * 插入 用户商品映射对象
     *
     * @param userProductMap 条件实体
     * @return 收影响行数
     */
    int insertUserProductMap(@Param("userProductMap") UserProductMap userProductMap);
}
