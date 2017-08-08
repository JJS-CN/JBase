package com.jjs.base.mvp;

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

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    public void send(BusBean busBean) {
        mBus.onNext(busBean);
    }

    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public void toBusBean(Consumer<BusBean> onNext) {
        mBus.ofType(BusBean.class).subscribe(onNext);
    }

    public void toBusBean(String action, OnRxListener onRxListener) {
        toBusBean(action, noType, onRxListener);
    }

    public void toBusBean(int type, OnRxListener onRxListener) {
        toBusBean(null, type, onRxListener);
    }

    public void toBusBean(final String action, final int type, final OnRxListener onRxListener) {
        mBus.ofType(BusBean.class).subscribe(new Consumer<BusBean>() {
            @Override
            public void accept(@NonNull BusBean busBean) throws Exception {
                if (onRxListener != null && (action != null && busBean.getAction() != null && action.equals(busBean.getAction()) || action == null) && (type == noType || type == busBean.getType()))
                    onRxListener.onRxListener(busBean.getData());
            }
        });
    }

    public interface OnRxListener {
        void onRxListener(Object data);
    }

    public Flowable<Object> toFlowable() {
        return mBus;
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }


}
