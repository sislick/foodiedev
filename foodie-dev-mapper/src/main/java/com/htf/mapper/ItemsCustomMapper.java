package com.htf.mapper;

import com.htf.my.mapper.MyMapper;
import com.htf.pojo.Items;
import com.htf.vo.ItemCommentVO;
import com.htf.vo.SearchItemsVO;
import com.htf.vo.ShopcartVO;
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

    List<SearchItemsVO> searchItemsByThirdCat(@Param("catId") Integer catId,
                                    @Param("sort") String sort);

    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    int decreaseItemSpecStock(@Param("specId") String specId, @Param("pendingCounts") Integer pendingCounts);
}