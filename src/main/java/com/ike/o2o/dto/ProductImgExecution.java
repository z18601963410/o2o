package com.ike.o2o.dto;


import com.ike.o2o.entity.ProductImg;
import com.ike.o2o.enums.ProductImgStateEnum;

import java.util.List;

public class ProductImgExecution {
    private int state;
    private String stateInfo;
    private ProductImg productImg;
    private List<ProductImg> productImgList;
    private int count;

    /**
     * 默认构造器
     */
    public ProductImgExecution() {
    }

    /**
     * 操作失败构造器
     *
     * @param productImgStateEnum
     */
    public ProductImgExecution(ProductImgStateEnum productImgStateEnum) {
        this.state = productImgStateEnum.getState();
        this.stateInfo = productImgStateEnum.getStateInfo();
    }

    /**
     * 操作成功时构造器
     *
     * @param productImgStateEnum 商品图片枚举对象
     * @param productImg          商品对象
     */
    public ProductImgExecution(ProductImgStateEnum productImgStateEnum, ProductImg productImg) {
        this.state = productImgStateEnum.getState();
        this.stateInfo = productImgStateEnum.getStateInfo();
        this.productImg = productImg;
    }

    /**
     * 操作成功时构造器
     *
     * @param productImgStateEnum 商品图片枚举对象
     * @param productImgList      商品对象list
     */
    public ProductImgExecution(ProductImgStateEnum productImgStateEnum, List<ProductImg> productImgList) {
        this.state = productImgStateEnum.getState();
        this.stateInfo = productImgStateEnum.getStateInfo();
        this.productImgList = productImgList;
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

    public ProductImg getProductImg() {
        return productImg;
    }

    public void setProductImg(ProductImg productImg) {
        this.productImg = productImg;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
