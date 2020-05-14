package com.htf.controller;

import com.htf.base.BaseController;
import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.pojo.Users;
import com.htf.response.ResponseJSONResult;
import com.htf.service.UserService;
import com.htf.utils.JsonUtils;
import com.htf.utils.MD5Utils;
import com.htf.utils.RedisOperator;
import com.htf.vo.UsersVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class SSOController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    private static final String REDIS_USER_TOKEN = "redis_user_token";
    private static final String REDIS_USER_TICKET = "redis_user_ticket";
    private static final String REDIS_TMP_TICKET = "redis_tmp_ticket";

    private static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    @GetMapping("/login")
    public Object login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws BusinessException{
        model.addAttribute("returnUrl", returnUrl);

        //1.获取userTicket门票，如果cookie中能够获取得到，证明用户登录过，此时签发一个一次性的临时票据并且回跳
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        if(verifyUserTicket(userTicket)){
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
        }

        //2.用户从未登录过，第一次进入则跳转到CAS的统一登录页面
        return "login";
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseJSONResult logout(String userId,
                        HttpServletRequest request,
                        HttpServletResponse response) throws BusinessException{
        //0.获取CAS中的用户门票
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        //1.清除userTicket票据，redis/cookie
        delCookie(COOKIE_USER_TICKET, response);
        redisOperator.del(REDIS_USER_TICKET + ":" + userTicket);

        //2.清除用户全局会话（分布式会话）
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return ResponseJSONResult.create(null);
    }

    private void delCookie(String key, HttpServletResponse response){
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    /**
     * 校验CAS全局用户门票
     * @param userTicket
     * @return
     */
    private boolean verifyUserTicket(String userTicket){
        //0.验证CAS全局用户门票不为空
        if(StringUtils.isBlank(userTicket)){
            return false;
        }
        //1.验证CAS门票是否有效
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if(StringUtils.isBlank(userId)){
            return false;
        }
        //2.验证门票对应的user是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if(StringUtils.isBlank(userRedis)){
            return false;
        }
        return true;
    }

    /**
     * CAS的统一登录接口
     *      目的：
     *          1.登录后创建用户的全局会话                  ->  unniqueToken
     *          2.创建用户全局门票，用以表示在CAS端是否登录    ->  userTicket
     *          3.创建用户的临时票据，用以回跳回传            ->  tmpTicket
     * @param username
     * @param password
     * @param returnUrl
     * @param model
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/doLogin")
    public Object doLogin(
                        String username,
                        String password,
                        String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){
        model.addAttribute("returnUrl", returnUrl);

        //1.判断用户名和密码不能为空
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }
        //2.实现登录
        Users users = userService.queryUsersForLogin(username, password);
        if(users == null){
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }
        //3.实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();


        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUniqueToken(uniqueToken);

        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(),
                JsonUtils.objectToJson(usersVO));

        //4.生成ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket = UUID.randomUUID().toString().trim();
        //4.1 用户全局门票需要放入CAS端的cookie中
        setCookie(COOKIE_USER_TICKET, userTicket, response);
        //5.userTicket关联用户id，并且放入到redis中，代表这个用户有门票了，可以在各个景区游玩
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, users.getId());

        //6.生成临时票据，回跳到调用端网站，是由CAS端所签发的一个一次性的临时ticket
        String tmpTicket = createTmpTicket();

        /**
         * userTicket:用于表示用户在CAS端的一个登陆状态：已登录
         * tmpTicket:用于颁发给用户进行一次性的验证的票据，有时效性
         */
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public ResponseJSONResult verifyTmpTicket(String tmpTicket,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws Exception {
        //使用一次性临时票据来验证用户是否登录，如果登录过，把用户会话信息返回给站点
        //使用完毕后，需要销毁临时票据
        String tmpTicketValue = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if(StringUtils.isBlank(tmpTicketValue)){
            throw new BusinessException(EmBusinessError.TMP_TICKET_ERROR);
        }
        //0.如果临时票据ok，则需要销毁，并且拿到CAS端cookie中的全局userTicket，以此再获取用户会话
        if(!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))){
            throw new BusinessException(EmBusinessError.TMP_TICKET_ERROR);
        }else{
            //销毁临时票据
            redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);
        }

        //1.验证并别获取用户的userTicket
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if(StringUtils.isBlank(userId)){
            throw new BusinessException(EmBusinessError.TMP_TICKET_ERROR);
        }

        //2.验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if(StringUtils.isBlank(userRedis)){
            throw new BusinessException(EmBusinessError.TMP_TICKET_ERROR);
        }

        //验证成功，返回OK，携带用户会话

        return ResponseJSONResult.create(JsonUtils.jsonToPojo(userRedis, UsersVO.class));
    }

    /**
     * 创建临时票据
     * @return
     */
    private String createTmpTicket(){
        String tmpTicket = UUID.randomUUID().toString().trim();
        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket,
                    MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;
    }

    private void setCookie(String key, String val, HttpServletResponse response){
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String getCookie(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if(cookies == null || StringUtils.isBlank(key)){
            return null;
        }
        String cookieValue = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(key)){
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }
}
