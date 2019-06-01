package com.wudi.datou.common.auth;


public class AuthUser {

    // 用户ID
    private long uid;

    // 子账号用户id
    private long sub_uid;

    // 过期时间戳，毫秒
    private long expire;

    public AuthUser(long uid, long expire) {
        this(uid, expire, 0);
    }

    public AuthUser(long uid, long expire, long sub_uid) {
        this.uid = uid;
        this.sub_uid = sub_uid;
        this.expire = expire;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getSub_uid() {
        return sub_uid;
    }

    public void setSub_uid(long sub_uid) {
        this.sub_uid = sub_uid;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public boolean isExpired() {
        return this.expire < System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "uid=" + uid +
                ", sub_uid=" + sub_uid +
                ", expire=" + expire +
                ", current=" + System.currentTimeMillis() +
                "}";
    }
}
