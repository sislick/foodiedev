package com.htf.service.impl;

import com.htf.enums.EmOrderStatus;
import com.htf.enums.YesOrNo;
import com.htf.error.BusinessException;
import com.htf.mapper.OrderItemsMapper;
import com.htf.mapper.OrderStatusMapper;
import com.htf.mapper.OrdersMapper;
import com.htf.pojo.*;
import com.htf.service.AddressService;
import com.htf.service.ItemService;
import com.htf.service.OrdersService;
import com.htf.vo.MerchantOrdersVO;
import com.htf.vo.OrderVO;
import com.htf.vo.SubmitOrdersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderVO createOrder(SubmitOrdersVO submitOrdersVO) throws BusinessException {
        String userId = submitOrdersVO.getUserId();
        String addressId = submitOrdersVO.getAddressId();
        String itemSpecIds = submitOrdersVO.getItemSpecIds();
        Integer payMethod = submitOrdersVO.getPayMethod();
        String leftMsg = submitOrdersVO.getLeftMsg();
        //包邮费用
        Integer postAmount = 0;

        String orderId = sid.nextShort();

        UserAddress address = addressService.queryUserAddress(userId, addressId);

        //1.新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince()
                                        + " " + address.getCity()
                                        + " " + address.getDistrict()
                                        + " " + address.getDetail());

        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        //2.循环根据itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;//商品原价累计
        Integer realPayAmount = 0;//优惠后的实际支付价格累计
        for(String itemSpecId : itemSpecIdArr){

            //TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;

            //2.1 根据规格Id，查询规格的具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            //2.2 根据规格id，获得商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String itemUrl = itemService.queryItemMainImgById(itemId);

            //2.3 循环保存子订单数据到数据库
            String subOrderItemId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();

            subOrderItem.setId(subOrderItemId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(itemUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(subOrderItem);

            //2.4 在用户提交订单后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);

        ordersMapper.insert(newOrder);

        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(EmOrderStatus.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());

        orderStatusMapper.insert(waitPayOrderStatus);

        //4.构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        //5.构建自定义VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();

        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }
}
