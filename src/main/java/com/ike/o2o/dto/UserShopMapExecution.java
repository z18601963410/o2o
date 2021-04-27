package com.ike.o2o.dto;


import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.entity.UserShopMap;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.enums.UserShopMapStateEnum;

import java.util.List;

public class UserShopMapExecution {
    private int state;
    private String stateInfo;

    private UserShopMap userShopMap;
    private List<UserShopMap> userShopMapList;

    private int count;

    //默认构造函数
    public UserShopMapExecution() {

    }

    //失败时构造函数
    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
    }

    //成功时构造函数
    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum, UserShopMap userShopMap) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    //成功时构造函数
    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum, List<UserShopMap> userShopMapList) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
