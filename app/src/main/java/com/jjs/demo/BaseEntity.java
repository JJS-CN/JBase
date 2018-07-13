package com.jjs.demo;

import com.jjs.base.entity.JBaseEntity;

/**
 * 说明：接受接口返回的实际 基础类，用于每个项目处理不一样字段规则
 * Created by jjs on 2018/7/2.
 */

public class BaseEntity extends JBaseEntity {
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
