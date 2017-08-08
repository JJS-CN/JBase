package com.jjs.base.bean;
/**
 * 说明：
 * 发送：RxBus.get().toBusBean
 * 接收：RxBus.get().send
 * Created by aa on 2017/6/30.
 */

public class BusBean {
    String action;//动作
    int type;//标识
    Object data;//内容

    public String getAction() {
        return action;
    }

    public BusBean setAction(String action) {
        this.action = action;
        return this;
    }

    public int getType() {
        return type;
    }

    public BusBean setType(int type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BusBean setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BusBean{" + "action='" + action + '\'' + ", type=" + type + ", data=" + data + '}';
    }
}