package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface OrderStatusMapper extends MyMapper<OrderStatus> {
}