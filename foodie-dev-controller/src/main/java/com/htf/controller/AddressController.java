package com.htf.controller;

import com.htf.pojo.UserAddress;
import com.htf.response.ResponseJSONResult;
import com.htf.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
@Api(value = "地址相关API", tags = "地址相关API")
public class AddressController {
    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1.查询用户的所有收货地址列表
     * 2.新增收货地址
     * 3.删除收货地址
     * 4.修改收货地址
     * 5.设置默认地址
     */

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    public ResponseJSONResult list(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId){
        List<UserAddress> userAddressList = addressService.queryAll(userId);

        return ResponseJSONResult.create(userAddressList);
    }
}
