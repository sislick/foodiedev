package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.enums.EmOrderStatus;
import com.htf.enums.EmPayMethod;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.service.OrdersService;
import com.htf.utils.CookieUtils;
import com.htf.vo.SubmitOrdersVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("orders")
@Api(value = "订单相关接口API", tags = "订单相关接口API")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/create")
    public ResponseJSONResult create(
            @RequestBody SubmitOrdersVO submitOrdersVO,
            HttpServletRequest request,
            HttpServletResponse response) throws BusinessException{
        if(submitOrdersVO.getPayMethod() != EmPayMethod.WEIXIN.type &&
                submitOrdersVO.getPayMethod() != EmPayMethod.ALIPAY.type){
            throw new BusinessException(EmBusinessError.ORDER_PAY_NOT_SUPPERT);
        }

        //1.创建订单
        String orderId = ordersService.createOrder(submitOrdersVO);
        //2.创建订单以后，溢出购物车中已结算的商品

        //TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        //CookieUtils.setCookie(request,response,"shopcart","", true);

        //3.向支付中心发送当前订单，用于保存支付中心的订单数据

        return ResponseJSONResult.create(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchanOrderId){

        ordersService.updateOrderStatus(merchanOrderId, EmOrderStatus.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }
}
