package com.ike.o2o.dto;


import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.entity.UserAwardMap;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

public class UserAwardMapExecution {
    private int state;
    private String stateInfo;

    private UserAwardMap userAwardMap;
    private List<UserAwardMap> userAwardMapList;

    private int count;

    //默认构造函数
    public UserAwardMapExecution() {

    }

    //失败时构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
    }

    //成功时构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum, UserAwardMap userAwardMap) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();

        this.userAwardMap = userAwardMap;
    }

    //成功时构造函数
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();

        this.userAwardMapList = userAwardMapList;
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

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
