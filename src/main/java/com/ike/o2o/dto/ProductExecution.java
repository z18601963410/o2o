package com.ike.o2o.dto;

import com.ike.o2o.entity.Product;
import com.ike.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {
    /*
    状态标示(int)、状态信息(String)、Bean对象、List<Bean>、count(int)Bean对象的数量、)
    构造器:默认构造器()、操作失败构造器(StateEnum stateEnum)、操作成功构造器(StateEnum stateEnum,实体类对象)、
    操作成功构造器(StateEnum stateEnum,List<StateEnum stateEnum>)
     */
    private int state;
    private String stateInfo;
    private Product product;
    private List<Product> productList;
    private int count;

    //默认构造器
    public ProductExecution() {
    }

    /**
     * 操作失败时构造器
     *
     * @param productStateEnum 商品状态枚举对象
     */
    public ProductExecution(ProductStateEnum productStateEnum) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }

    /**
     * 操作成功时构造器
     *
     * @param productStateEnum 商品状态枚举对象
     * @param product          商品对象
     */
    public ProductExecution(ProductStateEnum productStateEnum, Product product) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.product = product;
    }

    /**
     * 操作成功时构造器List
     *
     * @param productStateEnum 商品状态枚举对象
     * @param productList      商品list
     */
    public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList = productList;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
