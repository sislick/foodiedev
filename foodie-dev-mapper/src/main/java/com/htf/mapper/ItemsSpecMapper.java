package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.ItemsSpec;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ItemsSpecMapper extends MyMapper<ItemsSpec> {
}