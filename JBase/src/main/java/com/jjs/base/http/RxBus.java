package com.jjs.base.http;

import com.jjs.base.bean.BusBean;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 说明：
 * Created by aa on 2017/6/29.
 */

public class RxBus {

    private final FlowableProcessor<Object> mBus;
    private int noType = -1;
    //private static volatile RxBus mRxBus;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    /**
     * 获取单例实体
     */
    @Deprecated
    public static RxBus get() {
        //双重校验锁方式
       /* if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }*/
        //静态内部类方式
        return Holder.BUS;
    }

    public static RxBus getInstance() {
        //静态内部类方式
        return Holder.BUS;
    }

    /**
     * 调取发送方法
     */
    public void send(String action, int type, Object data) {
        this.send(new BusBean(action, type, data));
    }

    public void send(BusBean busBean) {
        mBus.onNext(busBean);
    }

    /**
     * 调用接收方法
     */
    public void toBusBean(OnRxListener onRxListener) {
        // mBus.ofType(BusBean.class).subscribe(onNext);
        this.toBusBean(null, noType, onRxListener);
    }

    public void toBusBean(String action, OnRxListener onRxListener) {
        this.toBusBean(action, noType, onRxListener);
    }

    public void toBusBean(int type, OnRxListener onRxListener) {
        this.toBusBean(null, type, onRxListener);
    }

    public void toBusBean(final String action, final int type, final OnRxListener onRxListener) {
        mBus.ofType(BusBean.class).subscribe(new Consumer<BusBean>() {
            @Override
            public void accept(@NonNull BusBean busBean) throws Exception {
                if (onRxListener != null && (action == null && busBean.getAction() == null || action != null && busBean.getAction() != null && action.equals(busBean.getAction())) && type == busBean.getType()) {
                    onRxListener.onRxListener(busBean.getData());
                }
            }
        });
    }

    /**
     * 基础方法，toBusBean方法的原型。
     * 将接收到的数据，转换为某class类型
     */
    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }


    public Flowable<Object> toFlowable() {
        return mBus;
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
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
    public interface OnRxListener {
        void onRxListener(Object data);
    }

}
