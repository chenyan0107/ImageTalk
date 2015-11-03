package com.chen.cy.talkimage.network.xvolley.rop;

/**
 * Created by xu on 2015/8/7.
 */
public class RopResult {
    /**
     * "code":"0","message":"操作成功","result"
     */
    public static final String CODE_SUCCESS = "0";
    public static final String CODE_SUCCESS_000000 = "000000";
    public static final String CODE_SUCCESS_P2P = "P2P000000";


    public static final String CODE_FAILED_SESSION_TIMEOUT = "21";

    private String code;

    private String message;

    private String result; //这里使用String来保存result ，后续操作来处理


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return CODE_SUCCESS.equals(code) || CODE_SUCCESS_P2P.equals(code) || CODE_SUCCESS_000000.equals(code);
    }
}
