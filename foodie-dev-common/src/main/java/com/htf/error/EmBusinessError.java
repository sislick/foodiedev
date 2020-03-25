package com.htf.error;

public enum EmBusinessError implements CommonError {

    //1开头，系统通用错误类型
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    //2开头，用户相关的异常
    USER_NOT_EXIST(20001,"用户名不存在"),
    USER_ALREADY_REGISTER(20002,"用户名已经注册"),
    USER_NULL(20003,"用户名为空"),
    PASSWORD_NOT_SAME(20004,"两次输入的密码不一致"),
    USER_OR_PASSWORD_ERROR(20005,"用户名或密码不正确"),
    USER_OR_PASSWORD_BLANK(20006,"用户名或密码不能为空"),

    //3开头，商品信息相关
    ITEM_NOT_EXIST(30001,"商品分类不存在")
    ;

    //错误编码
    private int errorCode;
    //错误信息
    private String errorMsg;

    private EmBusinessError(int errorCode,String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
