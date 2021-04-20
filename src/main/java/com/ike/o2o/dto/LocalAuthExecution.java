package com.ike.o2o.dto;


import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.enums.LocalAuthStateEnum;

import java.util.List;

public class LocalAuthExecution {
    private int state;
    private String stateInfo;

    private LocalAuth localAuth;
    private List<LocalAuth> localAuthList;

    private int count;

    //默认构造函数
    public LocalAuthExecution() {

    }

    //失败时构造函数
    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
    }

    //成功时构造函数
    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, LocalAuth localAuth) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();

        this.localAuth = localAuth;
    }

    //成功时构造函数
    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, List<LocalAuth> localAuthList) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();

        this.localAuthList = localAuthList;
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

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public List<LocalAuth> getLocalAuthList() {
        return localAuthList;
    }

    public void setLocalAuthList(List<LocalAuth> localAuthList) {
        this.localAuthList = localAuthList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
