package com.htf.interceptor;

import com.htf.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserTokenInterceptor implements HandlerInterceptor {

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 请求controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //System.out.println("进入了UserTokenInterceptor拦截器");
        String userToken = request.getHeader("headerUserToken");
        String  userId = request.getHeader("headerUserId");

        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
            String unqiueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if(StringUtils.isNotBlank(unqiueToken)){
                System.out.println("请登录");
                return false;
            }else{
                if(!unqiueToken.equals(userToken)){
                    System.out.println("账号在异地登录");
                    return false;
                }
            }
        }else{
            System.out.println("请登录");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
