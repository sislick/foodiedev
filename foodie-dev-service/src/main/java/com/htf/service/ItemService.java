package com.htf.service;

import com.htf.pojo.Items;
import com.htf.pojo.ItemsImg;
import com.htf.pojo.ItemsParam;
import com.htf.pojo.ItemsSpec;
import com.htf.utils.PagedGridResult;
import com.htf.vo.CommentLevelCountsVO;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品ID查询商品详情
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询评价数量信息
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);
}
