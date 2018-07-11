package com.jjs.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加注解在activity上，如果baseActivity设置了loginActivity将进行登陆检查
 * Created by felear on 2018/4/25.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {
    String[] value();
}
