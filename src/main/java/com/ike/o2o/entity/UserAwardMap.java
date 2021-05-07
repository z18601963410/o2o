package com.ike.o2o.entity;

import java.util.Date;

//顾客领取奖品映射
public class UserAwardMap {
    //ID
    private Long userAwardId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date lastEditTime;
    //领取奖品所消耗的积分
    private Integer point;
    //状态 0未兑换,1已兑换
    private Integer usedStatus;

    //归属用户
    private PersonInfo user;
    //归属店铺
    private Shop shop;
    //关联奖品
    private Award award;
    //操作员
    private PersonInfo operator;

    @Override
    public String toString() {
        return "UserAwardMap{" +
                "userAwardId=" + userAwardId +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", point=" + point +
                ", usedStatus=" + usedStatus +
                ", user=" + user +
                ", shop=" + shop +
                ", award=" + award +
                ", operator=" + operator +
                '}';
    }

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Long userAwardId) {
        this.userAwardId = userAwardId;
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

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }
}
