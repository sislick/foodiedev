package com.htf.controller;

import com.htf.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@Api(value = "redis相关的接口", tags = "redis相关的接口")
public class RedisController extends BaseController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/set")
    @ApiOperation(value = "set键值对", notes = "set键值对", httpMethod = "GET")
    public Object set(String key, String value){
        redisTemplate.opsForValue().set(key,value);
        return "成功";
    }

    @GetMapping("/get")
    @ApiOperation(value = "得到key的值", notes = "得到key的值", httpMethod = "GET")
    public Object get(String key){
        String value = (String)redisTemplate.opsForValue().get(key);
        return value;
    }
}
