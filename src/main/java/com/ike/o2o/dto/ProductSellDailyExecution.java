package com.ike.o2o.dto;


import com.ike.o2o.entity.ProductSellDaily;
import com.ike.o2o.entity.ProductSellDaily;
import com.ike.o2o.enums.ProductSellDailyStateEnum;

import java.util.List;

public class ProductSellDailyExecution {
    private int state;
    private String stateInfo;

    private ProductSellDaily productSellDaily;
    private List<ProductSellDaily> productSellDailyList;

    private int count;

    //默认构造函数
    public ProductSellDailyExecution() {

    }

    //失败时构造函数
    public ProductSellDailyExecution(ProductSellDailyStateEnum productSellDailyStateEnum) {
        this.state = productSellDailyStateEnum.getState();
        this.stateInfo = productSellDailyStateEnum.getStateInfo();
    }

    //成功时构造函数
    public ProductSellDailyExecution(ProductSellDailyStateEnum productSellDailyStateEnum, ProductSellDaily productSellDaily) {
        this.state = productSellDailyStateEnum.getState();
        this.stateInfo = productSellDailyStateEnum.getStateInfo();

        this.productSellDaily = productSellDaily;
    }

    //成功时构造函数
    public ProductSellDailyExecution(ProductSellDailyStateEnum productSellDailyStateEnum, List<ProductSellDaily> productSellDailyList) {
        this.state = productSellDailyStateEnum.getState();
        this.stateInfo = productSellDailyStateEnum.getStateInfo();

        this.productSellDailyList = productSellDailyList;
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

    public ProductSellDaily getProductSellDaily() {
        return productSellDaily;
    }

    public void setProductSellDaily(ProductSellDaily productSellDaily) {
        this.productSellDaily = productSellDaily;
    }

    public List<ProductSellDaily> getProductSellDailyList() {
        return productSellDailyList;
    }

    public void setProductSellDailyList(List<ProductSellDaily> productSellDailyList) {
        this.productSellDailyList = productSellDailyList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
