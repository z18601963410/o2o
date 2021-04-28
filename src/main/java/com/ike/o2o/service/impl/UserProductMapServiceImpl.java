package com.ike.o2o.service.impl;

import com.ike.o2o.dao.UserProductMapDao;
import com.ike.o2o.dto.UserProductMapExecution;
import com.ike.o2o.entity.UserProductMap;
import com.ike.o2o.enums.UserProductMapStateEnum;
import com.ike.o2o.service.UserProductMapService;
import com.ike.o2o.until.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;

    /**
     * 分页查询
     *
     * @param userProductMap 条件实体
     * @param pageIndex      页码
     * @param pageSize       每页size
     * @return
     */
    @Override
    public UserProductMapExecution getUserProductMap(UserProductMap userProductMap, Integer pageIndex, Integer pageSize) {
        //非空判断 页码不能为空
        if (userProductMap != null && pageIndex != null && pageSize != null) {
            //页码转化
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMap(userProductMap, rowIndex, pageSize);
            int count = userProductMapDao.queryUserProductMapCount(userProductMap);
            //数据封装
            UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
            userProductMapExecution.setCount(count);
            userProductMapExecution.setUserProductMapList(userProductMapList);
            userProductMapExecution.setState(UserProductMapStateEnum.SUCCESS.getState());
            return userProductMapExecution;
        }
        return new UserProductMapExecution();
    }
}
