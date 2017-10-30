package com.jjs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 说明：用于确定执行哪个方法的注解
 * Target表示作用于方法
 * Retention表示保留时长？
 * 注解名为RequestCode，默认值为0
 * Created by jjs on 2017/8/10.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCode {
    int code() default 0;
}
