package com.ike.o2o.dto;


import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.entity.UserProductMap;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.enums.UserProductMapStateEnum;

import java.util.List;

public class UserProductMapExecution {
    private int state;
    private String stateInfo;

    private UserProductMap userProductMap;
    private List<UserProductMap> userProductMapList;

    private int count;

    //默认构造函数
    public UserProductMapExecution() {

    }

    //失败时构造函数
    public UserProductMapExecution(UserProductMapStateEnum userProductMapStateEnum) {
        this.state = userProductMapStateEnum.getState();
        this.stateInfo = userProductMapStateEnum.getStateInfo();
    }

    //成功时构造函数
    public UserProductMapExecution(UserProductMapStateEnum userProductMapStateEnum, UserProductMap userProductMap) {
        this.state = userProductMapStateEnum.getState();
        this.stateInfo = userProductMapStateEnum.getStateInfo();

        this.userProductMap = userProductMap;
    }

    //成功时构造函数
    public UserProductMapExecution(UserProductMapStateEnum userProductMapStateEnum,List<UserProductMap> userProductMapList) {
        this.state = userProductMapStateEnum.getState();
        this.stateInfo = userProductMapStateEnum.getStateInfo();
        this.userProductMapList = userProductMapList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public UserProductMap getUserProductMap() {
        return userProductMap;
    }

    public void setUserProductMap(UserProductMap userProductMap) {
        this.userProductMap = userProductMap;
    }

    public List<UserProductMap> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
