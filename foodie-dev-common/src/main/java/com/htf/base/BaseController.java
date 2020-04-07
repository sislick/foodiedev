package com.htf.base;

import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务异常处理的controller
 */
public class BaseController {

    //微信支付成功-》支付中心-》天天吃货平台
    //                    -》回调通知的url
    public final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    //支付中心的调用地址
    public final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handleException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errorCode",businessException.getErrorCode());
            responseData.put("errorMsg",businessException.getErrorMsg());
        }else{
            responseData.put("errorCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errorMsg",EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
        }

        return ResponseJSONResult.create(responseData,"fail");
    }
}
