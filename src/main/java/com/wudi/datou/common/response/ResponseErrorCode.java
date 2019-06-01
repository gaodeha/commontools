package com.wudi.datou.common.response;

public enum ResponseErrorCode {

    //公共错误
    SUCCESS(0, "success"),

    ERROR(1000, "server err"),
    PARAM_ERR(1001, "err params"),

    //登陆认证相关
    USER_LOGIN_EXPIRE(2000, "user login session timeout"),
    USER_NOT_LOGIN(2001, "user has not login"),
    USER_INFO_ERROR(2002, "user info err"),
    USER_NOT_EXIST(2003, "user info is not exist"),
    USER_ALREADY_EXIST(2004, "user name has exist"),
    USER_NOT_MATCH_REGIST_TYPE(2005, "please login with the type of regist"),

    //上传相关
    UPLOAD_TOKEN_FAIL(3000, "get upload token fail"),

    //书本相关
    BOOK_NOT_EXIST(4000, "book is not exist"),
    BOOK_CANNOT_ACCESS(4001, "can not access this book"),
    FOLDER_NOT_EXIST(4002, "folder is not exist"),
    FOLDER_CANNOT_ACCESS(4003, "can not access this folder"),
    FOLDER_DEFAULT_CANNOT_DELETE(4004, "can not delete default folder"),

    //任务相关
    TASK_NOTEXIST(5000, "task not exist"),
    TASK_CANNOT_ACCESS(5001, "can not access this task info"),

    //子账号相关
    SUB_USER_HAS_EXIST(6000, "subuser name has exist"),
    SUB_USER_COUNT_HAS_BEYOND(6001, "sub user count has beyond max limit"),
    SUB_USER_DENY_CREATE(6002, "deny to create subuser"),
    // 修改账号
    CAN_NOT_MODIFY_PASSWORD(6003, "login from third website"),
    PASSWORD_NOT_CORRECT(6004, "password not correct"),
    ;


    int code;
    String msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResponseErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(int code) {
        for (ResponseErrorCode errorCode : ResponseErrorCode.values()) {
            if(code == errorCode.getCode()) {
                return errorCode.getMsg();
            }
        }
        return null;
    }

    public static ResponseErrorCode getByCode(int code) {
        for (ResponseErrorCode errorCode : ResponseErrorCode.values()) {
            if(code == errorCode.getCode()) {
                return errorCode;
            }
        }
        return null;
    }
}
