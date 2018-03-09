package com.jjs.demo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.jjs.R;
import com.jjs.base.base.BaseFragment;
import com.jjs.base.bean.RxBusBean;
import com.jjs.base.http.RxBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 说明：
 * Created by aa on 2018/1/3.
 */

public class AAFragment extends BaseFragment {


    @BindView(R.id.iv_1)
    ImageView mIv1;

    @Override
    protected int getLayoutId() {
        return R.layout.head;
    }

    @Override
    protected void initData(Bundle bundle) {
        mIv1.setImageResource(R.mipmap.ic_launcher);
        RxBus.with(this).setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus2");
            }
        });
        RxBus.with(this).setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus2.2");
            }
        });
        RxBus.with(this).setAction("1").setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus3");
            }
        });
        RxBus.with(this).setCode(1).setAction("2").setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus4");
            }
        });
        RxBus.with(this).setAction("1").setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus5");
            }
        });
        RxBus.with(this).setCode(2).setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus6");
            }
        });
    }


    @OnClick(R.id.iv_1)
    public void onViewClicked() {
        Toast.makeText(getActivity(), "111", Toast.LENGTH_SHORT).show();
    }
}
