package com.jjs.demo;

import android.content.Intent;

import com.jjs.base.base.BaseActivity;
import com.jjs.base.bean.RxBusBean;
import com.jjs.base.http.RxBus;

/**
 * 说明：
 * Created by aa on 2017/7/18.
 */

public class RxBusDemo extends BaseActivity {
    public void send() {
        RxBus.send(1, null, null);
        RxBus.with(this).setCode(1).setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }
}
