package com.github.gaodeha.common.auth;

import com.github.gaodeha.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;

public class AuthKeyHolder {

    private static String authSalt = null;
    private static String authKey = null;

    private static volatile boolean hasInit = false;

    public static void init(String authSalt, String authKey) {
        if (StringUtils.isBlank(authSalt) || StringUtils.isBlank(authKey) || authSalt.length() != Constants.AUTH_SALT_KEY_LENGTH || authKey.length() != Constants.AUTH_SALT_KEY_LENGTH) {
            throw new IllegalArgumentException("auth param length should be " + Constants.AUTH_SALT_KEY_LENGTH);
        }
        AuthKeyHolder.authSalt = authSalt;
        AuthKeyHolder.authKey = authKey;
        AuthKeyHolder.hasInit = true;
    }

    public static String getAuthSalt() {
        if (!hasInit) {
            throw new IllegalStateException("AuthKeyHolder should init!!!");
        }
        return authSalt;
    }

    public static String getAuthKey() {
        if (!hasInit) {
            throw new IllegalStateException("AuthKeyHolder should init!!!");
        }
        return authKey;
    }
}
