package com.htf.controller;

import com.htf.enums.IsShow;
import com.htf.pojo.Carousel;
import com.htf.pojo.Category;
import com.htf.response.ResponseJSONResult;
import com.htf.service.CarouseService;
import com.htf.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
@Api(value = "首页", tags = {"首页展示的相关接口"})
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class IndexController {

    @Autowired
    private CarouseService carouseService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResponseJSONResult carousel(){
        List<Carousel> carousels = carouseService.quertAll(IsShow.YES_SHOW.type);
        return ResponseJSONResult.create(carousels);
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
}
