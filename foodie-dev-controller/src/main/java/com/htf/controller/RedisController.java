package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.response.ResponseJSONResult;
import com.htf.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/redis")
@Api(value = "redis相关的接口", tags = "redis相关的接口")
public class RedisController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    @ApiOperation(value = "set键值对", notes = "set键值对", httpMethod = "GET")
    public ResponseJSONResult set(String key, String value){
        redisOperator.set(key,value);
        return ResponseJSONResult.create("成功");
    }

    @GetMapping("/get")
    @ApiOperation(value = "得到key的值", notes = "得到key的值", httpMethod = "GET")
    public ResponseJSONResult get(String key){
        String value = (String)redisOperator.get(key);
        return ResponseJSONResult.create(value);
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("userInfo","new user");
        session.setMaxInactiveInterval(3600);
        return "ok";
    }
}
