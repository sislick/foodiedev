package com.htf.controller;

import com.htf.enums.EmPayMethod;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.vo.SubmitOrdersVO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
@Api(value = "订单相关接口API", tags = "订单相关接口API")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrdersController {


    @PostMapping("/create")
    public ResponseJSONResult create(
            @RequestBody SubmitOrdersVO submitOrdersVO) throws BusinessException{
        if(submitOrdersVO.getPayMethod() != EmPayMethod.WEIXIN.type ||
                submitOrdersVO.getPayMethod() != EmPayMethod.ALIPAY.type){
            throw new BusinessException(EmBusinessError.ORDER_PAY_NOT_SUPPERT);
        }

        //1.创建订单
        //2.创建订单以后，溢出购物车中已结算的商品
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据

        return ResponseJSONResult.create(null);
    }
}
