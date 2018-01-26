package com.jjs.base.bean;

/**
 * 说明：
 * 发送：RxBus.get().toBusBean
 * 接收：RxBus.get().send
 * Created by aa on 2017/6/30.
 */

public class RxBusBean {
    String action;//动作，用于标识一个大致范围
    int code;//标识，用于具体标识
    Object data;//内容，用于放置全局传递的内容

    public RxBusBean(int code, String action, Object data) {
        this.code = code;
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RxBusBean{" + "action='" + action + '\'' + ", type=" + code + ", data=" + data + '}';
    }
}