
package com.ike.o2o.enums;

public enum HeadLineStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "头条信息为空");

    private int state;
    private String stateInfo;

    HeadLineStateEnum(int state, String stateInfo) {
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
    public static HeadLineStateEnum stateOf(int state) {
        for (HeadLineStateEnum headLineStateEnum : values()) {
            if (headLineStateEnum.getState() == state) {
                return headLineStateEnum;
            }
        }
        return null;
    }
}
