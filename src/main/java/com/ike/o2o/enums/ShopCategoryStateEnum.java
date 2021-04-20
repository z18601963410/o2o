package com.ike.o2o.enums;

/**
 * 店铺状态
 */
public enum ShopCategoryStateEnum {
    CHECK(0, "审核中"),
    OFFLINE(-1, "非法商品分类"),
    SUCCESS(1, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "内部系统错误"),
    NULL_SHOPID(-1002, "ShopCategoryId为空"),
    NULL_SHOP(-1003, "ShopCategory信息为空");

    private int state;
    private String stateInfo;

    ShopCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopCategoryStateEnum stateOf(int state) {
        for (ShopCategoryStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
