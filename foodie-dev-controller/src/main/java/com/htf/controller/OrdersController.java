package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.enums.EmOrderStatus;
import com.htf.enums.EmPayMethod;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.service.OrdersService;
import com.htf.utils.CookieUtils;
import com.htf.vo.MerchantOrdersVO;
import com.htf.vo.OrderVO;
import com.htf.vo.SubmitOrdersVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("orders")
@Api(value = "订单相关接口API", tags = "订单相关接口API")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RestTemplate restTemplate;

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
        OrderVO orderVO = ordersService.createOrder(submitOrdersVO);
        String orderId = orderVO.getOrderId();

        //2.创建订单以后，溢出购物车中已结算的商品

        //TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        //CookieUtils.setCookie(request,response,"shopcart","", true);

        //3.向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("paasword","imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<ResponseJSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, ResponseJSONResult.class);
        ResponseJSONResult paymentResult = responseEntity.getBody();

//        if("fail".equals(paymentResult.getStatus())){
//            throw new BusinessException(EmBusinessError.ORDER_ERROR);
//        }

        return ResponseJSONResult.create(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchanOrderId){

        ordersService.updateOrderStatus(merchanOrderId, EmOrderStatus.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }
}
