package com.ike.o2o.service.impl;

import com.ike.o2o.dao.ShopAuthMapDao;
import com.ike.o2o.dto.ShopAuthMapExecution;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.enums.ShopAuthMapStateEnum;
import com.ike.o2o.exception.ShopAuthMapOperationException;
import com.ike.o2o.service.ShopAuthMapService;
import com.ike.o2o.until.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired
    ShopAuthMapDao shopAuthMapDao;

    /**
     * 新增店铺授权
     * @param shopAuthMap 授权对象实体
     * @return
     * @throws ShopAuthMapOperationException
     */
    @Override
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //非空判断
        if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null) {
            int affect = -1;
            try {
                affect = shopAuthMapDao.insertShopAuth(shopAuthMap);
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("新增店铺授权时出现异常:" + e.getMessage());
            }
            //获取受影响行数
            if (affect > 0) {
                //插入成功
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
            }
            //插入失败
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
        } else {
            //非空判断失败,条件为空
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.EMPTY);
        }
    }

    @Override
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        //非空判断
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            int affect = -1;
            //一般是可用状态和title的修改
            try {
                //修改
                affect = shopAuthMapDao.updateShopAuth(shopAuthMap);
            } catch (Exception e) {
                //触发异常
                throw new ShopAuthMapOperationException("修改店铺授权时出现异常:" + e.getMessage());
            }
            if (affect > 0) {
                //操作成功
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
            } else {
                //修改失败
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.EMPTY);
        }
    }

    @Override
    public ShopAuthMapExecution queryShopAuthMapList(Long shopId, Integer pageIndex, Integer pageSize) {
        //非空判断
        if (shopId != null && pageIndex != null && pageSize != null) {
            //页转行
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            //获取结果列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, rowIndex, pageSize);
            //获取授权列表count
            int shopAuthMapCount = shopAuthMapDao.queryShopAuthCountByShopId(shopId);

            ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();
            shopAuthMapExecution.setCount(shopAuthMapCount);
            shopAuthMapExecution.setShopAuthMapList(shopAuthMapList);
            shopAuthMapExecution.setState(ShopAuthMapStateEnum.SUCCESS.getState());
            return shopAuthMapExecution;
        } else {
            return null;
        }
    }

    @Override
    public ShopAuthMap queryShopAuthMapById(Long shopAuthMapId) {
        return shopAuthMapDao.queryShopAuthById(shopAuthMapId);
    }
}
