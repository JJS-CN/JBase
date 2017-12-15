package com.jjs.demo;

import com.jjs.base.bean.BusBean;
import com.jjs.base.http.RxBus;

/**
 * 说明：
 * Created by aa on 2017/7/18.
 */

public class RxBusDemo {
    public void send() {
        //通过busbean发送消息
        RxBus.get().send(new BusBean("11111"));
        //接收busbean
        RxBus.get().toBusBean(new RxBus.OnRxListener() {
            @Override
            public void onRxListener(Object data) {

            }
        });

        //判断busbean中的action是否一致，判断type是否一致，一致则返回data
        RxBus.get().toBusBean("启动", 11, new RxBus.OnRxListener() {
            @Override
            public void onRxListener(Object data) {

            }
        });
        //只判断action并返回data
        RxBus.get().toBusBean("启动", new RxBus.OnRxListener() {
            @Override
            public void onRxListener(Object data) {

            }
        });
        //只判断type并返回data
        RxBus.get().toBusBean(1, new RxBus.OnRxListener() {
            @Override
            public void onRxListener(Object data) {

            }
        });

    }
}
