package com.htf.service;

import com.htf.error.BusinessException;
import com.htf.vo.SubmitOrdersVO;

import java.util.List;

public interface OrdersService {

    /**
     * 用于创建订单相关信息
     * @param submitOrdersVO
     */
    String createOrder(SubmitOrdersVO submitOrdersVO) throws BusinessException;

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    void updateOrderStatus(String orderId, Integer orderStatus);
}
