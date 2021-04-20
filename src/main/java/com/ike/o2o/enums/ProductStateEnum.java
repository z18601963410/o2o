package com.ike.o2o.enums;

import org.apache.ibatis.javassist.NotFoundException;

public enum ProductStateEnum {
    OFFLINE(-1, "非法商品"), DOWN(0, "下架"), SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"), EMPTY(-1002, "商品为空"),ILLEGAL_PARAM(-1003,"参数非法");

    private int state;
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据状态码获取枚举对象
     *
     * @param state
     * @return
     */
    public static ProductStateEnum stateOf(int state) {
        for (ProductStateEnum productStateEnum : values()) {
            if (productStateEnum.state == state) {
                return productStateEnum;
            }
        }
        throw new IllegalArgumentException("状态码非法");
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
