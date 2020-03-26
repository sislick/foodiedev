package com.htf.enums;

public enum EmCommentLevel {
    GOOD(1,"好评"),
    NORMAL(2,"中评"),
    BAD(3,"差评")
    ;
    public Integer type;
    public String value;

    private EmCommentLevel(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
