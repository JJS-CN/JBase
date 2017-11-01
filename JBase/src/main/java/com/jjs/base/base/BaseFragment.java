package com.jjs.base.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 说明：消息传递通过rxBus进行交互
 * 或者通过接口回调
 * Created by aa on 2017/11/1.
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected View rootView;
    protected Bundle mBundle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        this.mBundle = getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getLayoutId() == 0) {
            throw new RuntimeException("In the Fragment, You must set layoutId !=0");
        }
        rootView = inflater.inflate(getLayoutId(), container, false);

        return rootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initData();
}
