package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.service.UserService;
import com.htf.vo.UsersVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUsers")
    public ResponseJSONResult createUsers(UsersVO usersVO){
        UsersVO usersResult = userService.createUsers(usersVO);

        return ResponseJSONResult.create(usersResult);
    }

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     * @throws BusinessException
     */
    @GetMapping("/usernameIsExist")
    public ResponseJSONResult usernameIsExist(@RequestParam String username) throws BusinessException{
        //1.判断用户名不能为空
        if(StringUtils.isBlank(username)){
            throw new BusinessException(EmBusinessError.USER_NULL);
        }
        //2.查找注册的用户名是否存在
        if(userService.queryUsernameIsExist(username)){
            throw new BusinessException(EmBusinessError.USER_ALREADY_REGISTER);
        }
        //3.请求成功，用户名没有重复
        return ResponseJSONResult.create(null);
    }

}
