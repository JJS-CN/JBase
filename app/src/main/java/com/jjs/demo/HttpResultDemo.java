package com.jjs.demo;

import java.io.Serializable;

/**
 * 本页：接口最外层返回值判断，与retrofit有逻辑沟通
 * Created by jjs on 2017-03-24.
 * Email:994462623@qq.com
 */

public class HttpResultDemo<T> implements Serializable {
    boolean success;
    String msg;
    int errorCode;//-1
    T body;

    @Override
    public String toString() {
        return "HttpResultDemo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
