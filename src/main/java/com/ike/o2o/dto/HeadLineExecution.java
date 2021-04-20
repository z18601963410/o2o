package com.ike.o2o.dto;



import com.ike.o2o.entity.HeadLine;
import com.ike.o2o.enums.HeadLineStateEnum;

import java.util.List;

public class HeadLineExecution {

    private int state;
    private String stateInfo;
    private int count;

    private ImageHolder headLineImg;
    private HeadLine headLine;
    private List<HeadLine> headLineList;


    @Override
    public String toString() {
        return "HeadLineExecution{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", count=" + count +
                ", headLineImg=" + headLineImg +
                ", headLine=" + headLine +
                ", headLineList=" + headLineList +
                '}';
    }

    //默认构造函数
    public HeadLineExecution() {
    }

    /**
     * 失败时构造器
     *
     * @param headLineStateEnum 枚举对象
     */
    public HeadLineExecution(HeadLineStateEnum headLineStateEnum) {
        this.state = headLineStateEnum.getState();
        this.stateInfo = headLineStateEnum.getStateInfo();
    }

    /**
     * 成功时构造器
     *
     * @param headLineStateEnum 状态枚举对象
     * @param headLine          头条对象
     */
    public HeadLineExecution(HeadLineStateEnum headLineStateEnum, HeadLine headLine) {
        this.state = headLineStateEnum.getState();
        this.stateInfo = headLineStateEnum.getStateInfo();
        this.headLine = headLine;
    }

    /**
     * 成功时构造器
     *
     * @param headLineStateEnum 状态枚举对象
     * @param headLineList      头条列表
     */
    public HeadLineExecution(HeadLineStateEnum headLineStateEnum, List<HeadLine> headLineList) {
        this.state = headLineStateEnum.getState();
        this.stateInfo = headLineStateEnum.getStateInfo();
        this.headLineList = headLineList;
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

    public ImageHolder getHeadLineImg() {
        return headLineImg;
    }

    public void setHeadLineImg(ImageHolder headLineImg) {
        this.headLineImg = headLineImg;
    }

    public HeadLine getHeadLine() {
        return headLine;
    }

    public void setHeadLine(HeadLine headLine) {
        this.headLine = headLine;
    }

    public List<HeadLine> getHeadLineList() {
        return headLineList;
    }

    public void setHeadLineList(List<HeadLine> headLineList) {
        this.headLineList = headLineList;
    }
}
