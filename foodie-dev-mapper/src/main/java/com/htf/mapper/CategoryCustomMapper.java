package com.htf.mapper;

import com.htf.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryCustomMapper {

    List<CategoryVO> getSubCatList(Integer rootCatId);
}
