package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.pojo.Items;
import com.htf.pojo.ItemsImg;
import com.htf.pojo.ItemsParam;
import com.htf.pojo.ItemsSpec;
import com.htf.response.ResponseJSONResult;
import com.htf.service.ItemService;
import com.htf.utils.PagedGridResult;
import com.htf.vo.CommentLevelCountsVO;
import com.htf.vo.ItemInfoVO;
import com.htf.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
@Api(value = "商品接口", tags = "商品信息展示的相关接口")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/info/{itemId}")
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    public ResponseJSONResult a(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){

        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return ResponseJSONResult.create(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ResponseJSONResult queryCommentCounts(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId){

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return ResponseJSONResult.create(countsVO);
    }

    @ApiOperation(value = "分页查询商品评论", notes = "分页查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public ResponseJSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) throws BusinessException{
        if(StringUtils.isBlank(itemId)){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return ResponseJSONResult.create(pagedGridResult);
    }


    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ResponseJSONResult search(
            @ApiParam(name = "keywords", value = "关键词", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) throws BusinessException{
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return ResponseJSONResult.create(pagedGridResult);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ResponseJSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) throws BusinessException{
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        PagedGridResult pagedGridResult = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
        return ResponseJSONResult.create(pagedGridResult);
    }

    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", tags = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public ResponseJSONResult refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接规格的ids", required = true, example = "1,3,5")
            @RequestParam String itemSpecIds){
        if(StringUtils.isBlank(itemSpecIds)){
            return ResponseJSONResult.create(null);
        }
        List<ShopcartVO> shopcartVOList = itemService.queryItemsBySpecIds(itemSpecIds);

        return ResponseJSONResult.create(shopcartVOList);
    }
}
