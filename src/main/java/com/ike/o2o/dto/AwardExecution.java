package com.ike.o2o.dto;


import com.ike.o2o.entity.Award;
import com.ike.o2o.enums.AwardStateEnum;

import java.util.List;

public class AwardExecution {
    private int state;
    private String stateInfo;

    private Award award;
    private List<Award> awardList;

    private int count;

    //默认构造函数
    public AwardExecution() {

    }

    //失败时构造函数
    public AwardExecution(AwardStateEnum awardStateEnum) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
    }

    //成功时构造函数
    public AwardExecution(AwardStateEnum AwardStateEnum, Award award) {
        this.state = AwardStateEnum.getState();
        this.stateInfo = AwardStateEnum.getStateInfo();

        this.award = award;
    }

    //成功时构造函数
    public AwardExecution(AwardStateEnum AwardStateEnum, List<Award> awardList) {
        this.state = AwardStateEnum.getState();
        this.stateInfo = AwardStateEnum.getStateInfo();

        this.awardList = awardList;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
