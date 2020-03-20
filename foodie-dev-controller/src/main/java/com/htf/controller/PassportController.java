package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.response.ResponseJSONResult;
import com.htf.service.UserService;
import com.htf.vo.UsersVO;
import com.htf.volidator.ValidationResult;
import com.htf.volidator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidatorImpl validator;

    /**
     * 注册用户
     * @param usersVO
     * @return
     * @throws BusinessException
     */
    @PostMapping("/regist")
    public ResponseJSONResult regist(@RequestBody UsersVO usersVO) throws BusinessException {
        //1.校验用户名，密码，确认密码不为空，校验密码长度不能小于6位
        ValidationResult validationResult = this.validator.validator(usersVO);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }

        //2.校验密码和确认密码是否一致
        if(!usersVO.getPassword().equals(usersVO.getConfirmPassword())){//不一致
            throw new BusinessException(EmBusinessError.PASSWORD_NOT_SAME);
        }

        //3.用户是否已经存在
        if(userService.queryUsernameIsExist(usersVO.getUsername())){//用户已经存在
            throw new BusinessException(EmBusinessError.USER_ALREADY_REGISTER);
        }

        //4.注册用户
        UsersVO usersResult = userService.createUsers(usersVO);

        //5.返回响应结果给前端
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
