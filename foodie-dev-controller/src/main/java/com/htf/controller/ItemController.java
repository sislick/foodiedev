package com.htf.controller;

import com.htf.pojo.Items;
import com.htf.pojo.ItemsImg;
import com.htf.pojo.ItemsParam;
import com.htf.pojo.ItemsSpec;
import com.htf.response.ResponseJSONResult;
import com.htf.service.ItemService;
import com.htf.vo.CommentLevelCountsVO;
import com.htf.vo.ItemInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
@Api(value = "商品接口", tags = "商品信息展示的相关接口")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ItemController {

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

}
