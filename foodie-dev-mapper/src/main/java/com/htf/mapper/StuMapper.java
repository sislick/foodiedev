package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.Stu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StuMapper extends MyMapper<Stu> {
}