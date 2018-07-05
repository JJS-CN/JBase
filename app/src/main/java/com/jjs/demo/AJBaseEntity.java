package com.jjs.demo;

import com.jjs.base.entity.JBaseEntity;

/**
 * 说明：
 * Created by jjs on 2018/7/2.
 */

public class AJBaseEntity extends JBaseEntity {
    int a = 1;
    String message = "测试";

    @Override
    public boolean isSuccess() {
        return a == 1;
    }

    @Override
    public String message() {
        return message;
    }
}
