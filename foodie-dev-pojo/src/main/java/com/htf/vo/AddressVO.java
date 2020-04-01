package com.htf.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于新增或修改地址
 */
@Data
public class AddressVO {

    @ApiModelProperty(value = "地址id")
    private String addressId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "收件人姓名")
    private String receiver;
    @ApiModelProperty(value = "收件人手机号")
    private String mobile;
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "区县")
    private String district;
    @ApiModelProperty(value = "详细地址")
    private String detail;
}
