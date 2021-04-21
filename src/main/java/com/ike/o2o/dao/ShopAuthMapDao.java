package com.ike.o2o.dao;

import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopAuthMapDao {
    /**
     * 新增店铺授权
     *
     * @param shopAuthMap 授权对象
     * @return 影响行数
     */
    int insertShopAuth(ShopAuthMap shopAuthMap);

    /**
     * 除权
     *
     * @param shopAuthId 员工ID
     * @return 影响行数
     */
    int deleteShopAuthById(Long shopAuthId);

    /**
     * 更新授权信息
     *
     * @param shopAuthMap 新的授权对象实体
     * @return 影响行数
     */
    int updateShopAuth(@Param("shopAuthMap") ShopAuthMap shopAuthMap);

    /**
     * 根据员工ID查询授权对象信息
     *
     * @param shopAuthId 员工ID
     * @return 授权对象实体
     */
    ShopAuthMap queryShopAuthById(long shopAuthId);

    /**
     * 查询符合条件的授权对象
     *
     * @param ShopAuthMap 授权条件实体对象
     * @return 符合条件的列表
     */
    List<ShopAuthMap> queryShopAuthByCondition(@Param("shopAuthMapCondition") ShopAuthMap ShopAuthMap,@Param("rowIndex") int rowIndex, @Param("pageIndex") int pageIndex);

    /**
     * 指定店铺的授权名单列表
     *
     * @param shopId    店铺ID
     * @param rowIndex  起始行
     * @param pageIndex 每页长度
     * @return 分页数据
     */
    List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") Long shopId, @Param("rowIndex") int rowIndex, @Param("pageIndex") int pageIndex);

    /**
     * 获取店铺授权总数
     *
     * @param shopId 店铺ID
     * @return 总数
     */
    int queryShopAuthCountByShopId(long shopId);

}
