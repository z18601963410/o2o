package com.ike.o2o.service;

import com.ike.o2o.dto.ShopAuthMapExecution;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.exception.ShopAuthMapOperationException;

import java.util.List;

public interface ShopAuthMapService {
    /**
     * 新增授权对象
     *
     * @param shopAuthMap 授权对象实体
     * @return dto对象(状态信息)
     * @throws ShopAuthMapOperationException 运行时异常
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    /**
     * 修改授权对象
     *
     * @param shopAuthMap 对象实体
     * @return dto对象
     * @throws ShopAuthMapOperationException 运行时异常
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    /**
     * 分页查询符合条件的
     *
     * @param shopId   shopId
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @return 结果列表
     */
    ShopAuthMapExecution queryShopAuthMapList(Long shopId, Integer pageIndex, Integer pageSize);

    /**
     * 根据id查询授权对象
     *
     * @param shopAuthMapId 授权对象主键ID
     * @return 授权对象
     */
    ShopAuthMap queryShopAuthMapById(Long shopAuthMapId);
}
