package com.htf.controller;

import com.htf.response.ResponseJSONResult;
import com.htf.vo.OrdersVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
@Api(value = "订单相关接口API", tags = "订单相关接口API")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrdersController {


    @PostMapping("/create")
    public ResponseJSONResult create(
            @RequestBody OrdersVO ordersVO){

        System.out.println(ordersVO.toString());

        return ResponseJSONResult.create(null);
    }
}
