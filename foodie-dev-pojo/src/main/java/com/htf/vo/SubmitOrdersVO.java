package com.htf.vo;

import lombok.Data;

@Data
public class SubmitOrdersVO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;

}
