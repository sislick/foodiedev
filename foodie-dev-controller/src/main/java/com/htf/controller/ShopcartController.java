package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("shopcart")
@Api(value = "购物车接口controller", tags = "购物车接口相关的api")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ShopcartController extends BaseController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public ResponseJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartVO shopcartVO,
            HttpServletRequest request,
            HttpServletResponse response
    )throws BusinessException{
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }

        System.out.println(shopcartVO);

        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return ResponseJSONResult.create(null);
    }
}
