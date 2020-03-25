package com.htf.mapper;

import com.htf.vo.CategoryVO;
import com.htf.vo.NewItemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CategoryCustomMapper {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewItemsVO> getSixNewItemLazy(@Param("paramsMap") Map<String, Object> map);
}
