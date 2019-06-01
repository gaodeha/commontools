package com.wudi.datou.common.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class CORSInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CORSInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String reqOrigin = request.getHeader("Origin");
        if(StringUtils.isBlank(reqOrigin)){
            reqOrigin = getOriginFromReferer(request);
        }
        if(StringUtils.isBlank(reqOrigin)){
            reqOrigin = "*";
        }
        response.setHeader("content-type", "application/json;charset=UTF-8");
        response.setHeader("Access-Control-Request-Method", "*");
        response.setHeader("Access-Control-Allow-Origin", reqOrigin);
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie, *");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    private String getOriginFromReferer(HttpServletRequest request) {
        String reqOrigin = null;
        String reqReferer = request.getHeader("Referer");
        if(StringUtils.isBlank(reqReferer)){
            try {
                URL url = new URL(reqReferer);
                StringBuilder sb = new StringBuilder();
                sb.append(url.getProtocol());
                sb.append("://");
                sb.append(url.getHost());
                if(url.getPort() != 80){
                    sb.append(url.getPort());
                }
                reqOrigin = sb.toString();
            } catch (Exception ignore) {
            }
        }
        return reqOrigin;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
