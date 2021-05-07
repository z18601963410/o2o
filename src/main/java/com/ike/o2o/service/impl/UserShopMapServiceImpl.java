package com.ike.o2o.service.impl;

import com.ike.o2o.dao.UserShopMapDao;
import com.ike.o2o.dto.UserShopMapExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.enums.UserAwardMapStateEnum;
import com.ike.o2o.enums.UserShopMapStateEnum;
import com.ike.o2o.exception.UserShopMapOperationException;
import com.ike.o2o.service.UserShopMapService;
import com.ike.o2o.until.PageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    private final Logger logger = LoggerFactory.getLogger(UserShopMapServiceImpl.class);
    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 插入顾客店铺映射对象,顾客在店铺首次消费时执行
     *
     * @param userShopMap 映射对象实体
     * @return UserShopMapExecution
     * @throws UserShopMapOperationException 运行时异常
     */
    @Override
    public UserShopMapExecution insertUserShopMap(UserShopMap userShopMap) throws UserShopMapOperationException {
        UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
        //非空判断
        if (userShopMap != null && userShopMap.getShop() != null && userShopMap.getShop().getShopId() != null
                && userShopMap.getUser() != null && userShopMap.getUser().getUserId() != null) {
            //参数初始化
            userShopMap.setCreateTime(new Date());
            try {
                //插入
                int affect = userShopMapDao.insertUserShopMap(userShopMap);
                //影响结果判断
                if (affect > 0) {
                    //操作成功标识
                    userShopMapExecution.setState(UserShopMapStateEnum.SUCCESS.getState());
                    //当前映射对象添加到DTO中
                    userShopMapExecution.setUserShopMap(userShopMap);
                } else {
                    //操作结果标识
                    userShopMapExecution.setState(UserShopMapStateEnum.INNER_ERROR.getState());
                }
                //返回
                return userShopMapExecution;
            } catch (Exception e) {
                //记录日志
                logger.error(e.getMessage());
                //抛出异常
                throw new UserShopMapOperationException("顾客店铺映射异常:" + e.getMessage());
            }

        }
        //空值
        userShopMapExecution.setState(UserShopMapStateEnum.EMPTY.getState());
        //返回
        return userShopMapExecution;
    }

    /**
     * 修改顾客积分
     *
     * @param userShopMapCondition 修改条件实体 必须包含ShopId
     * @return 修改后的对象实体
     * @throws UserShopMapOperationException 运行时异常
     */
    @Override
    public UserShopMapExecution updateUserShopMapPoint(UserShopMap userShopMapCondition) throws UserShopMapOperationException {
        //DTO
        UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
        if (userShopMapCondition != null && userShopMapCondition.getShop() != null && userShopMapCondition.getShop().getShopId() != null
                && userShopMapCondition.getUser() != null && userShopMapCondition.getUser().getUserId() != null
        ) {
            int affect;
            try {
                affect = userShopMapDao.updateUserShopMap(userShopMapCondition);
            } catch (Exception e) {
                logger.error(e.getMessage());
                //返回异常
                throw new UserShopMapOperationException("顾客商铺映射更新异常:" + e.getMessage());
            }
            if (affect > 0) {
                userShopMapExecution.setState(UserShopMapStateEnum.SUCCESS.getState());
            } else {
                userShopMapExecution.setState(UserShopMapStateEnum.INNER_ERROR.getState());
            }
            //返回DTO
            return userShopMapExecution;
        }
        userShopMapExecution.setState(UserShopMapStateEnum.EMPTY.getState());
        //返回DTO
        return userShopMapExecution;
    }

    /**
     * 查询符合条件的顾客商铺映射对象,返回分页数据
     *
     * @param userShopMapCondition userShopMapCondition
     * @param pageIndex            分页页码
     * @param pageSize             每页size
     * @return 分页数据
     */
    @Override
    public UserShopMapExecution queryUserShopMapListByShop(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
        UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
        //非空判断
        if (userShopMapCondition != null) {
            //计算rowIndex
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            try {
                //查询数据
                List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopList(userShopMapCondition, rowIndex, pageSize);
                //查询数据长度
                int count = userShopMapDao.queryUserShopListCount(userShopMapCondition);
                //数据封装
                userShopMapExecution.setCount(count);
                userShopMapExecution.setUserShopMapList(userShopMapList);
                //成功状态
                userShopMapExecution.setState(UserShopMapStateEnum.SUCCESS.getState());
                return userShopMapExecution;
            } catch (Exception e) {
                //记录日志
                logger.error(e.getMessage());
                //返回异常
                throw new UserShopMapOperationException("查询异常");
            }
        }
        userShopMapExecution.setState(UserShopMapStateEnum.EMPTY.getState());
        return userShopMapExecution;
    }

    public UserShopMapExecution queryUserShopMapByUserId(long userId, long shopId) {
        UserShopMapExecution userShopMapExecution = new UserShopMapExecution();
        try {
            UserShopMap userShopMap = userShopMapDao.queryUserShopMapById(userId, shopId);
            if (userShopMap != null) {
                userShopMapExecution.setState(UserAwardMapStateEnum.SUCCESS.getState());
                userShopMapExecution.setUserShopMap(userShopMap);
                return userShopMapExecution;
            } else {
                userShopMapExecution.setState(UserAwardMapStateEnum.INNER_ERROR.getState());
                userShopMapExecution.setStateInfo("查询userShopMap为null");
                return userShopMapExecution;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UserShopMapOperationException("查询顾客积分异常");
        }
    }

    /**
     * 查询顾客所有的积分
     *
     * @param userId 顾客ID
     * @return
     */
    public Integer queryUserPointById(long userId) {
        return userShopMapDao.queryAllPointByUserId(userId);
    }
}
