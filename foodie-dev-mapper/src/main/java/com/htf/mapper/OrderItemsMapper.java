package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.OrderItems;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderItemsMapper extends MyMapper<OrderItems> {
}