package com.htf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "用户VO对象", description = "用于前后端交互的数据对象")
public class UsersVO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名",name = "username",example = "小马",required = true)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6,message = "密码长度不能小于6位")
    @ApiModelProperty(value = "密码",name = "password",example = "123123",required = true)
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码",name = "confirmPassword",example = "123123",required = true)
    private String confirmPassword;
}
