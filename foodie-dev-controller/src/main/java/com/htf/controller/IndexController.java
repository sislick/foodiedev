package com.htf.controller;

import com.htf.enums.IsShow;
import com.htf.pojo.Carousel;
import com.htf.response.ResponseJSONResult;
import com.htf.service.CarouseService;
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

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResponseJSONResult carousel(){
        List<Carousel> carousels = carouseService.quertAll(IsShow.YES_SHOW.type);
        return ResponseJSONResult.create(carousels);
    }
}
