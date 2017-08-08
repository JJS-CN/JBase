package com.jjs.base.Permission;

/**
 * 本页：
 * Created by jjs on 2017-04-12.
 * Email:994462623@qq.com
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注通过权限申请
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionSucceed {
    int value() default -1;
}
