package com.jjs.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 说明：
 * Created by aa on 2017/10/30.
 */

public class AnnotationDemo {
    /**
     * 反射处理。查询类所有方法
     */
    private void invokeMethod() {
        //this.getClass() 不管继承多少层，拿到拿到最终实例化的那个class类；后方法拿到这个类下所有的方法
        Method[] methods = this.getClass().getDeclaredMethods();
        //有发起请求时，遍历所有数据
        for (int i = 0; i < methods.length; i++) {
            //遍历方法中是否包含此注解，切判断注解中值是否为对应值
            if (methods[i].isAnnotationPresent(RequestCode.class) && methods[i].getAnnotation(RequestCode.class).code() == 1) {
                try {
                    //判断通过时调用对应方法：1从底层方法调用的对象，2方法调用的参数
                    methods[i].invoke(this, "参数");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
