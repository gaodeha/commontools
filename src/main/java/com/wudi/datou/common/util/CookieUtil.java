package com.wudi.datou.common.util;


import com.wudi.datou.common.auth.AuthUser;
import com.wudi.datou.common.auth.AuthUtil;
import com.wudi.datou.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private final static Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public static void setCookies(HttpServletResponse response, String authUser) {
        Cookie cookie = new Cookie(Constants.HEADER_AUTH_TOKEN, authUser);
        cookie.setMaxAge(Constants.TOKEN_EXPIRE_SECONDS);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setCookies(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(Constants.TOKEN_EXPIRE_SECONDS);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    public static void clearCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.HEADER_AUTH_TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
