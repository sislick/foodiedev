package com.htf.vo;

import lombok.Data;

/**
 * 用于新增或修改地址
 */
@Data
public class AddressVO {

    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;
}
