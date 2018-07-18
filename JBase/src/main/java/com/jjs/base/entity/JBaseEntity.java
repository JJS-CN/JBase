package com.jjs.base.entity;

/**
 * 说明：接收数据 基础类中的基础类,需要继承此类并重写2个方法
 * Created by jjs on 2018/7/2.
 */

public abstract class JBaseEntity {

    //判断是否请求成功。服务器都会设定一个统一成功码
    public abstract boolean isSuccess();

    //错误内容
    public abstract String message();

}
