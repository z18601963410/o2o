package com.ike.o2o.enums;

/**
 * 商铺商品对象状态枚举类,用于封装商铺商品对象
 */
public enum ProductCategoryStateEnum {
    OFFLINE(-1, "非法商品分类"),
    DOWN(0, "下架"),
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "商品分类为空");

    private int state;
    private String stateInfo;

    //默认构造函数
    ProductCategoryStateEnum() {
    }

    //有参构造函数
    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ProductCategoryStateEnum stateOf(int index) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == index) {
                return productCategoryStateEnum;
            }
        }
        return null;
    }
}
