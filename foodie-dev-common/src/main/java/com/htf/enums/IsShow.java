package com.htf.enums;

public enum IsShow {
    NO_SHOW(0,"否"),
    YES_SHOW(1,"是");

    public final Integer type;
    public final String value;

    IsShow(Integer type,String value){
        this.type = type;
        this.value = value;
    }
}
