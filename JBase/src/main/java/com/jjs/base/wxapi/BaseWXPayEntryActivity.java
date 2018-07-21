package com.jjs.base.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.rxbus.RxBus;
import com.jjs.base.BaseStore;
import com.jjs.base.pay.PayResult;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付的回调界面
 * 1：在baseStore中修改对应的值wxAppId
 * 2：实际项目继承此activity。
 * 3：在主项目中AndroidManifest.xml中添加当前activity
 */
public class BaseWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, BaseStore.wxAppId, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        PayResult result = new PayResult(resp);
        RxBus.getDefault().post(result, BaseStore.Pay_Result);
        finish();
    }
}