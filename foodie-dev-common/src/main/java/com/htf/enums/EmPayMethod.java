package com.htf.enums;

/**
 * 支付方式枚举类
 */
public enum  EmPayMethod {
    WEIXIN(1,"微信"),
    ALIPAY(2,"支付宝")
    ;
    public final Integer type;
    public final String value;

    EmPayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
