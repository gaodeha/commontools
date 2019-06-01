package com.wudi.datou.common.filter;


import com.alibaba.fastjson.JSON;
import com.wudi.datou.common.auth.AuthChecker;
import com.wudi.datou.common.auth.AuthUser;
import com.wudi.datou.common.auth.CheckLogin;
import com.wudi.datou.common.response.ResponseErrorCode;
import com.wudi.datou.common.response.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class LoginInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        AuthUser authUser = AuthChecker.checkCurrentLoginUser(request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method= handlerMethod.getMethod();
            //方法上有该标记
            Annotation methodAnnotation = method.getAnnotation(CheckLogin.class);
            if(methodAnnotation == null) {
                return true;
            }
            ResultVO<String> rep = null;
            if(authUser == null) {
                logger.warn("user not login, request url:" + request.getServletPath());
                rep = ResultVO.failResult(ResponseErrorCode.USER_NOT_LOGIN);
            } else if(authUser.isExpired()) {
                logger.warn("user login expire, request url:" + request.getServletPath());
                rep = ResultVO.failResult(ResponseErrorCode.USER_LOGIN_EXPIRE);
            } else {
                // 验证登陆成功
                //response.setHeader(Constants.HEADER_AUTH_TOKEN, AuthUtil.encrypt(authUser.getUid()));
                return true;
            }
            // 返回需要登陆的错误提示信息
            String data = JSON.toJSONString(rep);
            response.setHeader("content-type", "application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            out.write(data.getBytes("UTF-8"));
            return false;
        }
        return true;
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        AuthUser authUser = AuthChecker.get();
//        if (authUser != null) {
//            CookieUtil.setCookies(response, authUser);
//        }
    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
