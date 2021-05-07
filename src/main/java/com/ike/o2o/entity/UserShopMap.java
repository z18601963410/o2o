package com.ike.o2o.entity;

import java.util.Date;

//顾客与铺积分关系映射
public class UserShopMap {
    //主键ID
    private Long userShopId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date lastEditTime;
    //顾客在该店铺的积分
    private Integer point;
    //顾客实体
    private PersonInfo user;
    //店铺实体
    private Shop shop;

    @Override
    public String toString() {
        return "UserShopMap{" +
                "userShopId=" + userShopId +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", point=" + point +
                ", user=" + user +
                ", shop=" + shop +
                '}';
    }

    public Long getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(Long userShopId) {
        this.userShopId = userShopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}

