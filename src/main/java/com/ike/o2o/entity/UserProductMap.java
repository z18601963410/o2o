package com.ike.o2o.entity;

import java.util.Date;

//顾客与商品的映射
public class UserProductMap {
    //主键ID
    private Long userProductId;
    //商品积分
    private Integer point;
    //创建时间
    private Date createTime;
    //顾客实体
    private PersonInfo user;
    //商品实体
    private Product product;
    //店铺实体
    private Shop shop;
    //操作员对象
    private PersonInfo operator;

    @Override
    public String toString() {
        return "UserProductMap{" +
                "userProductId=" + userProductId +
                ", point=" + point +
                ", createTime=" + createTime +
                ", user=" + user +
                ", product=" + product +
                ", shop=" + shop +
                ", operator=" + operator +
                '}';
    }

    public Long getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Long userProductId) {
        this.userProductId = userProductId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }
}
