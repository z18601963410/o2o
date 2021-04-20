package com.ike.o2o.dto;

import com.ike.o2o.entity.Shop;
import com.ike.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * 处理前端的shop对象
 */
public class ShopExecution {
    //状态标识
    private int state;

    //状态信息
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop(增删改店铺时用到)
    private Shop shop;

    //shop列表(查询)
    private List<Shop> shopList;

    //默认构造器
    public ShopExecution() {

    }

    //店铺操作失败时使用的构造器
    public ShopExecution(ShopStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //店铺操作成功时使用的构造器
    public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shop = shop;
    }

    //店铺操作成功时使用的构造器(多个)
    public ShopExecution(ShopStateEnum stateEnum, List<Shop> shop) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopList = shop;
    }

    @Override
    public String toString() {
        return "ShopExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", count=" + count +
                ", shop=" + shop +
                ", shopList=" + shopList +
                '}';
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
