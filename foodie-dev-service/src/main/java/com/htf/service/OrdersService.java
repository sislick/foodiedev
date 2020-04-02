package com.htf.service;

import com.htf.error.BusinessException;
import com.htf.vo.SubmitOrdersVO;

import java.util.List;

public interface OrdersService {

    /**
     * 用于创建订单相关信息
     * @param submitOrdersVO
     */
    void createOrder(SubmitOrdersVO submitOrdersVO) throws BusinessException;
}
