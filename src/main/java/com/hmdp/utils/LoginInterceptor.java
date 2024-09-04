package com.hmdp.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截未登录，上层有3个true，前两个都需要被拦截定向去登录，最后一个不用，区别在于ThreadLocal里有没有user
 */
public class LoginInterceptor implements HandlerInterceptor {

    //true放行，false拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果ThreadLocal里有user，则放行
        if(UserHolder.getUser() == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
