package com.jjs.base.mvp;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.bean.HashBean;
import com.jjs.base.http.JJsApiService;
import com.jjs.base.http.RequestCode;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.http.RxSchedulers;
import com.jjs.base.http.RxSubject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;

/**
 * 说明：使用rxjava和retrofit时，使用Lifecycle进行防内存溢出问题
 * Created by aa on 2017/6/28.
 */

public abstract class BasePersenter<V extends BaseView> {
    //view层
    public V mView;
    //存储实例化类的所有方法
    private Method[] methods;

    public BasePersenter(V view) {
        this.mView = view;
        //this.getClass() 不管继承多少层，拿到拿到最终实例化的那个class类；后方法拿到这个类下所有的方法
        methods = this.getClass().getDeclaredMethods();
    }


    public void destroy() {
        mView = null;
        methods = null;
    }

    /**
     * 统一使用此方法发送请求，此方法根据code查询子类所有注解方法进行code匹配后调用对应方法
     * 所有继承P层，子类请求方法都使用注解进行标识：RequestCode，在此注解中设置code。
     *
     * @param hash 请求参数包装，需要传入code参数
     */
    public void sendRequest(HashBean hash) {
        if (StringUtils.isEmpty(hash.getUrl()))
            invokeMethod(hash);
        else
            sendHttp(hash);

    }

    /**
     * url为空，查询code注解方法正常执行
     *
     * @param hash
     */
    public void invokeMethod(HashBean hash) {
        //有发起请求时，遍历所有数据
        for (int i = 0; i < methods.length; i++) {
            //遍历方法中是否包含此注解，切判断注解中值是否为对应值
            if (methods[i].isAnnotationPresent(RequestCode.class) && methods[i].getAnnotation(RequestCode.class).code() == hash.getRequestCode()) {
                try {
                    //判断通过时调用对应方法：1从底层方法调用的对象，2方法调用的参数
                    methods[i].invoke(this, hash);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * url有值，替换请求地址为url，请求参数直接使用map集合请求
     *
     * @param hash
     */
    public void sendHttp(final HashBean hash) {
        JJsApiService.Request request;
        Observable<String> observable;
        if (hash.isHasHeader()) {
            request = RetrofitUtils.getInstanceHeaders().create(JJsApiService.Request.class);
        } else {
            request = RetrofitUtils.getInstance().create(JJsApiService.Request.class);
        }
        if (hash.isGET()) {
            observable = request.sendGet(hash.getUrl(), hash.getHashMap());
        } else {
            observable = request.sendPost(hash.getUrl(), hash.getHashMap());
        }
        observable.compose(RxSchedulers.io_main(hash.isShowLoading(), mView.bindToLifecycle()))
                .subscribe(new RxSubject<String>() {
                    @Override
                    protected void _onNext(String data) {
                        mView.ResponseSuccess(hash.getRequestCode(), data);
                    }

                    @Override
                    protected void _onComplete() {

                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
    }

}
