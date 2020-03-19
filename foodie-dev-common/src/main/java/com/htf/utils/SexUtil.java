package com.htf.utils;

import lombok.Data;

/**
 * 性别枚举类
 */
public enum  SexUtil {

    WOMAN(0,"女"),
    MAN(1,"男"),
    SECRECY(2,"保密");

    public final int type;
    public final String value;

    SexUtil(int type,String value){
        this.type = type;
        this.value = value;
    }
}
