package com.ike.o2o.dto;


import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.entity.ShopAuthMap;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.enums.ShopAuthMapStateEnum;

import java.util.List;

public class ShopAuthMapExecution {
    private int state;
    private String stateInfo;

    private ShopAuthMap shopAuthMap;
    private List<ShopAuthMap> shopAuthMapList;

    private int count;

    //默认构造函数
    public ShopAuthMapExecution() {

    }

    //失败时构造函数
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum) {
        this.state = shopAuthMapStateEnum.getState();
        this.stateInfo = shopAuthMapStateEnum.getStateInfo();
    }

    //成功时构造函数
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum, ShopAuthMap shopAuthMap) {
        this.state = shopAuthMapStateEnum.getState();
        this.stateInfo = shopAuthMapStateEnum.getStateInfo();

        this.shopAuthMap = shopAuthMap;
    }

    //成功时构造函数
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum, List<ShopAuthMap> shopAuthMapList) {
        this.state = shopAuthMapStateEnum.getState();
        this.stateInfo = shopAuthMapStateEnum.getStateInfo();

        this.shopAuthMapList = shopAuthMapList;
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

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
