package com.ike.o2o.enums;

public enum ProductImgStateEnum {
    OFFLINE(-1, "非法商品图片"), DOWN(0, "下架"), SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "操作失败"), EMPTY(-1002, "商品图片为空");
    private int state;
    private String stateInfo;

    ProductImgStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据状态码返回枚举对象
     *
     * @param state 状态码
     * @return
     */
    public static ProductImgStateEnum stateOf(int state) {
        for (ProductImgStateEnum productImgStateEnum : values()) {
            if (productImgStateEnum.state == state) {
                return productImgStateEnum;
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
