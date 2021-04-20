package com.ike.o2o.dto;


import com.ike.o2o.entity.ShopCategory;
import com.ike.o2o.enums.ShopCategoryStateEnum;

import java.util.List;

/**
 * 处理前端的shopCategory对象
 */
public class ShopCategoryExecution {
    //状态标识
    private int state;

    //状态信息
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop(增删改店铺时用到)
    private ShopCategory shopCategory;

    //shop列表(查询)
    private List<ShopCategory> shopCategoryList;

    //默认构造器
    public ShopCategoryExecution() {

    }

    //店铺操作失败时使用的构造器
    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    //店铺操作成功时使用的构造器
    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum, ShopCategory shopCategory) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopCategory = shopCategory;
    }

    //店铺操作成功时使用的构造器(多个)
    public ShopCategoryExecution(ShopCategoryStateEnum stateEnum, List<ShopCategory> shopCategoryList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopCategoryList = shopCategoryList;
    }


    @Override
    public String toString() {
        return "ShopCategoryExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", count=" + count +
                ", shopCategory=" + shopCategory +
                ", shopCategoryList=" + shopCategoryList +
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

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    public List<ShopCategory> getShopCategoryList() {
        return shopCategoryList;
    }

    public void setShopCategoryList(List<ShopCategory> shopCategoryList) {
        this.shopCategoryList = shopCategoryList;
    }
}
