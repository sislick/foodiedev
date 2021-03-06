package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.enums.IsShow;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.pojo.Carousel;
import com.htf.pojo.Category;
import com.htf.response.ResponseJSONResult;
import com.htf.service.CarouseService;
import com.htf.service.CategoryService;
import com.htf.utils.JsonUtils;
import com.htf.utils.RedisOperator;
import com.htf.vo.CategoryVO;
import com.htf.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huotengfei
 */
@RestController
@RequestMapping("index")
@Api(value = "首页", tags = {"首页展示的相关接口"})
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class IndexController extends BaseController {

    @Autowired
    private CarouseService carouseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResponseJSONResult carousel(){
        List<Carousel> list = new ArrayList<>();

        String carouselsStr = redisOperator.get("carousels");
        if(StringUtils.isBlank(carouselsStr)){
            list = carouseService.quertAll(IsShow.YES_SHOW.type);
            redisOperator.set("carousels", JsonUtils.objectToJson(list));
        }else{
            carouselsStr = redisOperator.get("carousels");
            list = JsonUtils.jsonToList(carouselsStr, Carousel.class);
        }

        return ResponseJSONResult.create(list);
    }

    /**
     * 首页分类展示需求：
     * 1.第一次刷新首页查询大分类，渲染展示到首页
     * 2.如果鼠标移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */

    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public ResponseJSONResult category(){

        List<Category> categoryList = categoryService.queryAllRootLevelCat();

        return ResponseJSONResult.create(categoryList);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ResponseJSONResult subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) throws BusinessException{
        if(rootCatId == null){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }

        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);

        return ResponseJSONResult.create(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ResponseJSONResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) throws BusinessException{

        if(rootCatId == null){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
    }

        List<NewItemsVO> newItemLazy = categoryService.getSixNewItemLazy(rootCatId);

        return ResponseJSONResult.create(newItemLazy);

    }
}
