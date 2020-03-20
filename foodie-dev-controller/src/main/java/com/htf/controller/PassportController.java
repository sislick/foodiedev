package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.pojo.Users;
import com.htf.response.ResponseJSONResult;
import com.htf.service.UserService;
import com.htf.vo.UsersVO;
import com.htf.volidator.ValidationResult;
import com.htf.volidator.ValidatorImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@ApiIgnore API文档不显示此controller
@Api(value = "注册登录", tags = {"用于登录注册的相关接口"})
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidatorImpl validator;

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "用户名是否存在", notes = "判断用户名是否存在的API", httpMethod = "GET")
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

    /**
     * 注册用户
     * @param usersVO
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "用户注册", notes = "用户注册API", httpMethod = "POST")
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

    @ApiOperation(value = "用户登录", notes = "用户登录API", httpMethod = "POST")
    @PostMapping("/login")
    public ResponseJSONResult login(@RequestBody UsersVO usersVO) throws BusinessException{
        //1.校验参数非空
        if(usersVO == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if(StringUtils.isBlank(usersVO.getUsername()) ||
            StringUtils.isBlank(usersVO.getPassword())){
            throw new BusinessException(EmBusinessError.USER_OR_PASSWORD_BLANK);
        }

        //2.检索用户名和密码是否匹配
        Users users = userService.queryUsersForLogin(usersVO.getUsername(), usersVO.getPassword());
        if(users == null){
            throw new BusinessException(EmBusinessError.USER_OR_PASSWORD_ERROR);
        }

        //3.返回响应结果给前端
        return ResponseJSONResult.create(users);
    }

}
