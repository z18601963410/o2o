package com.ike.o2o.dto;

import com.ike.o2o.entity.ProductCategory;
import com.ike.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

/**
 * ProductCategory的DTO对象
 */
public class ProductCategoryExecution {
    //状态标示
    private int state;
    //状态信息
    private String stateInfo;
    //封装的对象
    private ProductCategory productCategory;
    //封装的对象列表
    private List<ProductCategory> productCategoryList;
    //对象的数量
    private int count;

    //默认构造器
    public ProductCategoryExecution() {
    }

    //操作成功时构造器,说明:当店铺成功被操作时将店铺对象和店铺的状态信息封装到DTO中供前端使用
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum, ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
    }

    //操作成功时构造器(多个对象)
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum, List<ProductCategory> productCategoryList) {
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }

    //操作失败时构造器  将错误状态和错误状态描述封装到DTO中
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum) {
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
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

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ProductCategoryExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", productCategory=" + productCategory +
                ", productCategoryList=" + productCategoryList +
                ", count=" + count +
                '}';
    }
}
