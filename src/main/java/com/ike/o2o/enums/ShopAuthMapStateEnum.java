
package com.ike.o2o.enums;

public enum ShopAuthMapStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "信息为空");

    private int state;
    private String stateInfo;

    ShopAuthMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 根据状态码返回对应的状态对象
     *
     * @param state 状态码
     * @return 状态对象
     */
    public static ShopAuthMapStateEnum stateOf(int state) {
        for (ShopAuthMapStateEnum temp : values()) {
            if (temp.getState() == state) {
                return temp;
            }
        }
        return null;
    }
}
