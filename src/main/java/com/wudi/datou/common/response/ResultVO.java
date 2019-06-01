package com.wudi.datou.common.response;

public class ResultVO<T> {

    private Integer code;

    private String message;

    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static ResultVO failResult(ResponseErrorCode errorCode) {
        ResultVO<String> vo = new ResultVO<String>();
        vo.setCode(errorCode.getCode());
        vo.setMessage(errorCode.getMsg());
        vo.setData(null);
        return vo;
    }

    public static <W> ResultVO<W> failResult(ResponseErrorCode errorCode, W data) {
        ResultVO<W> vo = new ResultVO<W>();
        vo.setCode(errorCode.getCode());
        vo.setMessage(errorCode.getMsg());
        vo.setData(data);
        return vo;
    }

    public static <W> ResultVO<W> successResult(W data) {
        ResultVO<W> vo = new ResultVO<W>();
        vo.setCode(ResponseErrorCode.SUCCESS.getCode());
        vo.setMessage(ResponseErrorCode.SUCCESS.getMsg());
        vo.setData(data);
        return vo;
    }
}
