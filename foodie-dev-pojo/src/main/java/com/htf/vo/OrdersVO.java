package com.htf.vo;

import lombok.Data;

@Data
public class OrdersVO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;

}
