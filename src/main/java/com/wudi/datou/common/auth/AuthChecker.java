package com.wudi.datou.common.auth;

import com.wudi.datou.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AuthChecker {

    private final static Logger logger = LoggerFactory.getLogger(AuthChecker.class);

    private static ThreadLocal<AuthUser> authLocal = new ThreadLocal<AuthUser>();

    /**
     * 外部调用需要检查authUser是否过期
     * @param request
     * @return
     */
    public static AuthUser checkCurrentLoginUser(HttpServletRequest request) {
        AuthUser user = null;
        String authToken = request.getHeader(Constants.HEADER_AUTH_TOKEN);

        // 头部没有认证字段，从cookie获取，方便swagger-ui测试
        if(StringUtils.isBlank(authToken)) {
            Cookie[] cookies = request.getCookies();
            if(null != cookies && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if(!Constants.HEADER_AUTH_TOKEN.equalsIgnoreCase(cookie.getName())) {
                        continue;
                    }
                    authToken = cookie.getValue();
                    break;
                }
            }
        }

        if(StringUtils.isBlank(authToken)) {
            authLocal.remove();
        }else {
            try {
                user = AuthUtil.decrypt(authToken);
                if(user != null ) {
                    authLocal.set(user);
                }
            } catch (Exception e) {
                logger.error("checkCurrentLoginUser exception", e);
                authLocal.remove();
            }
        }
        return user;
    }


    public static AuthUser get() {
        return authLocal.get();
    }
    public static void remove() { authLocal.remove(); }
}
