package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.Items;
import com.htf.vo.ItemCommentVO;
import com.htf.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ItemsCustomMapper {

    List<ItemCommentVO> queryItemComments(@Param("itemId") String itemId,
                           @Param("level") Integer level);

    List<SearchItemsVO> searchItems(@Param("keywords") String keywords,
                                    @Param("sort") String sort);
}