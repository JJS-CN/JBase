package com.jjs.base.bean;

/**
 * 说明：
 * 发送：RxBus.get().toBusBean
 * 接收：RxBus.get().send
 * Created by aa on 2017/6/30.
 */

public class BusBean {
    String action;//动作，用于标识一个大致范围
    int type;//标识，用于具体标识
    Object data;//内容，用于放置全局传递的内容

    public BusBean(Object data) {
       this(null, -1, data);
    }

    public BusBean(String action, Object data) {
        this(action, -1, data);
    }

    public BusBean(int type, Object data) {
        this(null, type, data);
    }


    public BusBean(String action, int type, Object data) {
        this.action = action;
        this.type = type;
        this.data = data;
    }

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