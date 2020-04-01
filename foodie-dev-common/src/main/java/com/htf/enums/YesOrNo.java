package com.htf.enums;

public enum YesOrNo {
    YES(1,"是"),
    NO(0,"否")
    ;
    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
