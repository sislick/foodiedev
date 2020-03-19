package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UsersMapper extends MyMapper<Users> {
}