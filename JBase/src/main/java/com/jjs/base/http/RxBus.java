package com.jjs.base.http;

import com.blankj.utilcode.util.StringUtils;
import com.jjs.base.bean.RxBusBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 说明：
 * Created by aa on 2017/6/29.
 */

public class RxBus {

    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    /**
     * 发送
     */
    public static void send(int code, String action, Object data) {
        Holder.BUS.mBus.onNext(new RxBusBean(code, action, data));
    }

    /**
     * 接收
     */
    public static ClassBind with(RxAppCompatActivity activity) {
        return new ClassBind(activity);
    }

    public static ClassBind with(com.trello.rxlifecycle2.components.RxFragment fragment) {
        return new ClassBind(fragment);
    }

    public static ClassBind with(com.trello.rxlifecycle2.components.support.RxFragment fragment) {
        return new ClassBind(fragment);
    }

    public static class ClassBind {
        private RxAppCompatActivity mActivity;
        private com.trello.rxlifecycle2.components.RxFragment mFragment;
        private com.trello.rxlifecycle2.components.support.RxFragment mSupportFragment;
        private int code;
        private String action;

        public ClassBind(RxAppCompatActivity activity) {
            this.mActivity = activity;
        }

        public ClassBind(com.trello.rxlifecycle2.components.RxFragment fragment) {
            this.mFragment = fragment;
        }

        public ClassBind(com.trello.rxlifecycle2.components.support.RxFragment fragment) {
            this.mSupportFragment = fragment;
        }

        public ClassBind setCode(int code) {
            this.code = code;
            return this;
        }

        public ClassBind setAction(String action) {
            this.action = action;
            return this;
        }

        public void setListener(final OnRxBusListener listener) {
            Holder.BUS.mBus.ofType(RxBusBean.class)
                    .filter(new Predicate<RxBusBean>() {
                        @Override
                        public boolean test(@NonNull RxBusBean rxBusBean) throws Exception {
                            //code相同，并且action都为空 或 action都不为空且相同
                            return rxBusBean.getCode() == code
                                    && (StringUtils.isEmpty(rxBusBean.getAction()) && StringUtils.isEmpty(action) || !StringUtils.isEmpty(rxBusBean.getAction()) && !StringUtils.isEmpty(action) && rxBusBean.getAction().equals(action));
                        }
                    })
                    .compose(mActivity != null ? mActivity.<RxBusBean>bindToLifecycle() : (mFragment != null ? mFragment.<RxBusBean>bindToLifecycle() : mSupportFragment.<RxBusBean>bindToLifecycle()))
                    .subscribe(new Consumer<RxBusBean>() {
                        @Override
                        public void accept(RxBusBean rxBusBean) throws Exception {
                            if (listener != null) {
                                listener.onBusListener(rxBusBean);
                            }
                        }
                    });
        }
    }

    /**
     * 静态内部类方式创建单例
     * 由于类加载机制，而且应用未使用到这个类时，jvm虚拟机不会去创建实例（懒加载）
     */
    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }

    /**
     * 监听方法
     */
    public interface OnRxBusListener {
        void onBusListener(RxBusBean busBean);
    }

}
