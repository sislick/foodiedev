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
    USER_NOT_LOGIN(20007,"用户名未登录"),

    //3开头，商品信息相关
    ITEM_NOT_EXIST(30001,"商品分类不存在"),

    //4开头，地址信息相关
    ADDRESS_USER_NOT_NULL(40001,"收货人不能为空"),
    ADDRESS_USER_NAME_TOO_LONG(40002,"收货人姓名不能太长"),
    ADDRESS_USER_MOBILE_NOT_NULL(40003,"收货人手机号不能为空"),
    ADDRESS_USER_MOBILE_LENGTH(40004,"收货人手机号长度不正确"),
    ADDRESS_USER_MOBILE_FORMAT(40005,"收货人手机号格式不正确"),
    ADDRESS_INFO_NOT_NULL(40006,"收货地址信息不能为空"),
    ADDRESS_UPDATE_ERROR(40007,"修改地址错误:addressId不能为空"),
    ADDRESS_DELETE_ERROR(40008,"删除地址错误:参数格式不正确")
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
