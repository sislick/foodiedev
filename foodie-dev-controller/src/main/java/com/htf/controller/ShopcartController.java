package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return ResponseJSONResult.create(null);
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public ResponseJSONResult del(
            @ApiParam(name = "userId", value = "用户id")
            @RequestParam String userId,
            @ApiParam(name = "itemSpecId", value = "商品规格id")
            @RequestParam String itemSpecId) throws BusinessException{
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //TODO 用户在页面删除购物车中的数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品

        return ResponseJSONResult.create(null);
    }
}
