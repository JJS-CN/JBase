package com.jjs.base.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明：消息传递通过rxBus进行交互
 * 或者通过接口回调
 * Created by aa on 2017/11/1.
 */

public abstract class BaseFragment extends RxFragment {
    protected Activity mActivity;
    protected View rootView;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            throw new RuntimeException("In the Fragment, You must getLayoutId() return !=0");
        }
        rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(getArguments());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (rootView == null) {
            super.setUserVisibleHint(false);
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    /**
     * 返回view布局id
     */
    protected abstract int getLayoutId();

    /**
     * 在与activity绑定时获取bundle，并进行初始化操作
     */
    protected abstract void initData(Bundle bundle);

}
