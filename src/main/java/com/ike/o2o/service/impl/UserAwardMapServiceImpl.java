package com.ike.o2o.service.impl;

import com.ike.o2o.dao.UserAwardMapDao;
import com.ike.o2o.dao.UserShopMapDao;
import com.ike.o2o.dto.UserAwardMapExecution;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.entity.Shop;
import com.ike.o2o.entity.UserAwardMap;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.enums.UserAwardMapStateEnum;
import com.ike.o2o.exception.UserAwardMapOperationException;
import com.ike.o2o.service.UserAwardMapService;
import com.ike.o2o.until.PageCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    private Logger logger = LoggerFactory.getLogger(UserAwardMapServiceImpl.class);
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 查询顾客积分映射列表分页数据
     *
     * @param userAwardMapCondition 条件实体
     * @param pageIndex             分页页码
     * @param pageSize              分页size
     * @return
     */
    @Override
    public UserAwardMapExecution queryUserAwardMapByShop(UserAwardMap userAwardMapCondition, int pageIndex, int pageSize) {
        UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
        //非空判断
        if (userAwardMapCondition != null) {
            //计算检索行
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            try {
                //查询数据
                List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMapCondition, rowIndex, pageSize);
                //计算行
                int count = userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);

                //数据封装
                userAwardMapExecution.setCount(count);
                userAwardMapExecution.setUserAwardMapList(userAwardMapList);
                //成功状态标识
                userAwardMapExecution.setState(UserAwardMapStateEnum.SUCCESS.getState());
                //返回数据
                return userAwardMapExecution;
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new UserAwardMapOperationException("顾客奖品兑换记录查询异常");
            }

        } else {
            userAwardMapExecution.setState(UserAwardMapStateEnum.EMPTY.getState());
            return userAwardMapExecution;
        }
    }

    /**
     * 新增顾客消费(积分)记录
     *
     * @param userAwardMap 条件实体
     * @return DTO
     * @throws UserAwardMapOperationException 运行异常,用于数据回滚
     */
    @Override
    public UserAwardMapExecution insertUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
        //非空判断
        if (userAwardMap != null && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null
                && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null && userAwardMap.getAward() != null &&
                userAwardMap.getAward().getAwardId() != null) {
            try {
                //初始化参数
                userAwardMap.setCreateTime(new Date());
                //默认没有兑换, 0未兑换,1已兑换
                userAwardMap.setUsedStatus(0);
                //插入新的兑换记录
                int affect_UserAwardMap = userAwardMapDao.insertUserAwardMap(userAwardMap);
                //顾客积分同步减少
                UserShopMap userShopMap = new UserShopMap();
                userShopMap.setPoint(userAwardMap.getPoint() * -1);

                PersonInfo user = new PersonInfo();
                Shop shop = new Shop();
                user.setUserId(userAwardMap.getUser().getUserId());
                shop.setShopId(userAwardMap.getShop().getShopId());

                userShopMap.setUser(user);
                userShopMap.setShop(shop);
                int affect_UserShopMap = userShopMapDao.updateUserShopMap(userShopMap);
                if (affect_UserAwardMap > 0 && affect_UserShopMap > 0) {
                    //数据封装并返回
                    userAwardMapExecution.setState(UserAwardMapStateEnum.SUCCESS.getState());
                    userAwardMapExecution.setUserAwardMap(userAwardMap);
                    return userAwardMapExecution;
                } else {
                    userAwardMapExecution.setState(UserAwardMapStateEnum.INNER_ERROR.getState());
                    return userAwardMapExecution;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new UserAwardMapOperationException("新增顾客奖品消费记录异常");
            }
        }
        userAwardMapExecution.setState(UserAwardMapStateEnum.EMPTY.getState());
        return userAwardMapExecution;
    }

    /**
     * @param userAwardId userAwardId
     * @return
     */
    public UserAwardMapExecution queryUserAwardMapById(Long userAwardId) {
        UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
        //非空判断
        if (userAwardId != null) {
            try {
                //dao查询
                UserAwardMap userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardId);
                if (userAwardMap != null) {
                    userAwardMapExecution.setUserAwardMap(userAwardMap);
                    userAwardMapExecution.setState(UserAwardMapStateEnum.SUCCESS.getState());
                    return userAwardMapExecution;
                } else {
                    userAwardMapExecution.setState(UserAwardMapStateEnum.INNER_ERROR.getState());
                    userAwardMapExecution.setStateInfo("userAwardMapExecution is null !");
                    return userAwardMapExecution;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new UserAwardMapOperationException("UserAwardMap查询异常");
            }

        } else {
            userAwardMapExecution.setState(UserAwardMapStateEnum.EMPTY.getState());
            return userAwardMapExecution;
        }
    }


    public UserAwardMapExecution updateUserAwardMap(UserAwardMap userAwardMapCondition) {
        UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution();
        if (userAwardMapCondition != null && userAwardMapCondition.getUserAwardId() != null) {
            try {
                int affect = userAwardMapDao.updateUserAwardMap(userAwardMapCondition);
                if (affect == 1) {
                    userAwardMapExecution.setState(UserAwardMapStateEnum.SUCCESS.getState());
                    return userAwardMapExecution;
                } else {
                    userAwardMapExecution.setState(UserAwardMapStateEnum.INNER_ERROR.getState());
                    return userAwardMapExecution;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new UserAwardMapOperationException("userAwardMapDao修改时发生异常");
            }
        }
        userAwardMapExecution.setState(UserAwardMapStateEnum.EMPTY.getState());
        return userAwardMapExecution;
    }

}
