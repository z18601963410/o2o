package com.ike.o2o.dto;

/**
 * 封装所有的json对象,返回结果使用该对象进行封装
 */
public class Result<T> {
    //成功标志
    private boolean success;
    //成功时返回的数据
    private T data;
    //错误信息
    private String errMsg;
    //错误代码
    private int errCode;

    //成功时构造函数
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //错误时构造函数
    public Result(boolean success, int errCode, String errMsg) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    //返回成功标志
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
