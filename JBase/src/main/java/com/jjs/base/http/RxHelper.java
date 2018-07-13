package com.jjs.base.http;

import com.jjs.base.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 说明：自己根据实际逻辑实现此。mvp应该换种方式
 * Created by jjs on 2018/7/13.
 */

public class RxHelper {
    private boolean hasShowDialog = true;
    private RxAppCompatActivity mRxAppCompatActivity;
    private RxFragment mRxFragment;
    private RxDialogFragment mRxDialogFragment;


    public RxHelper(RxAppCompatActivity rxActivity) {
        this.mRxAppCompatActivity = rxActivity;
    }


    public RxHelper(RxFragment rxFragment) {
        this.mRxFragment = rxFragment;
    }

    public RxHelper(RxDialogFragment rxDialogFragment) {
        this.mRxDialogFragment = rxDialogFragment;
    }

    public static RxHelper getNewInstance(RxAppCompatActivity rxAppCompatActivity) {
        return new RxHelper(rxAppCompatActivity);
    }

    public static RxHelper getNewInstance(RxFragment rxFragment) {
        return new RxHelper(rxFragment);
    }

    public static RxHelper getNewInstance(RxDialogFragment rxDialogFragment) {
        return new RxHelper(rxDialogFragment);
    }

    public <T> ObservableTransformer<T, T> bind() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (hasShowDialog) {
                                    //todo mvp不适用
                                    LoadingDialog.show(mRxAppCompatActivity != null ? mRxAppCompatActivity : mRxFragment != null ? mRxFragment.getActivity() : mRxDialogFragment != null ? mRxDialogFragment.getActivity() : null);
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .compose(mRxAppCompatActivity != null ? mRxAppCompatActivity.<T>bindToLifecycle() : mRxFragment != null ? mRxFragment.<T>bindToLifecycle() : mRxDialogFragment != null ? mRxDialogFragment.<T>bindToLifecycle() : null);
            }
        };
    }
}
