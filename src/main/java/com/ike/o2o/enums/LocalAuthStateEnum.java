package com.ike.o2o.enums;


public enum LocalAuthStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "信息为空"),
    IS_BINDING(0, "对象已绑定");

    private int state;
    private String stateInfo;

    LocalAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
