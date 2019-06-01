package com.wudi.datou.common.auth;

import com.wudi.datou.common.util.CodecUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtil.class);


    public static String encrypt(long uid, int expireSeconds) throws Exception {
        return encrypt(uid, expireSeconds, AuthKeyHolder.getAuthSalt(), AuthKeyHolder.getAuthKey());
    }

    public static String encrypt(AuthUser authUser) throws Exception {
        return encrypt(authUser, AuthKeyHolder.getAuthSalt(), AuthKeyHolder.getAuthKey());
    }


    private static String encrypt(long uid, int expireSeconds, String authSalt, String authKey) throws Exception {
        if(uid <=0 || expireSeconds <= 0) {
            throw new IllegalArgumentException("uid or expireHours must be positive");
        }
        long expireMilliSeconds = expireSeconds * 1000;
        long expireTimeStamp = System.currentTimeMillis() + expireMilliSeconds;
        AuthUser authUser = new AuthUser(uid, expireTimeStamp);
        return encrypt(authUser, authSalt, authKey);
    }

    private static String encrypt(AuthUser authUser, String authSalt, String authKey) throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(24 + authSalt.length());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(authUser.getUid()).putLong(authUser.getExpire()).putLong(authUser.getSub_uid());
        bb.put(authSalt.getBytes());
        return CodecUtils.encryptBytes(authKey, bb.array());
    }


    public static AuthUser decrypt(String authToken) throws Exception {
        return decrypt(authToken, AuthKeyHolder.getAuthSalt(), AuthKeyHolder.getAuthKey());
    }

    private static AuthUser decrypt(String authToken, String authSalt, String authKey) throws Exception {
        byte[] orgBytes = CodecUtils.decryptToBytes(authKey, authToken);
        if(orgBytes.length != 16 + authSalt.length() && orgBytes.length != 32 + authSalt.length()) {
            logger.error("decrypt authToken error, expected length=32 or 48, actual length=" + orgBytes.length);
            throw new RuntimeException("authToken is invalid");
        }
        ByteBuffer bb = ByteBuffer.wrap(orgBytes).order(ByteOrder.LITTLE_ENDIAN);
        long uid = bb.getLong();
        long expireTimeStamp = bb.getLong();
        long sub_uid = 0;
        if (orgBytes.length > 16) {
            sub_uid = bb.getLong();
        }

        byte[] saltBytes = new byte[authSalt.length()];
        bb.get(saltBytes);
        String saltStr = new String(saltBytes);
        if( !authSalt.equals(saltStr) ) {
            logger.error("decrypt authToken error, expected=" + authSalt + ", actual salt=" + saltStr);
            throw new RuntimeException("authToken is invalid");
        }
        AuthUser authUser = new AuthUser(uid, expireTimeStamp, sub_uid);
        if(authUser.isExpired()) {
            logger.warn("decrypt authToken is expired, " + authUser.toString());
        }
        return authUser;
    }


    public static void main(String[] args) throws Exception {

        String authSalt = "23w4nz$(3&92%#za";
        String authKey = "s23a37&8623H^&!@";

        String authToken = AuthUtil.encrypt(10033, 1, authSalt, authKey);

        System.out.println(authToken);

        String authToken1 = AuthUtil.encrypt(10002, 1, authSalt, authKey);

        System.out.println(authToken1);

        String authToken2 = AuthUtil.encrypt(5003, 1, authSalt, authKey);

        System.out.println(authToken2);

        AuthUser user = AuthUtil.decrypt(authToken2, authSalt, authKey);

        System.out.println(user.getUid());

    }
}
